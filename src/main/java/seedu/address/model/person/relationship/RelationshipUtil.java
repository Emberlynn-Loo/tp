package seedu.address.model.person.relationship;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.person.relationship.Relationship.validDescriptors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.ResultContainer;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;


/**
 * Represents a utility class for managing the relationships associated with a person.
 * Allows for adding, deleting, and checking for existing relationships.
 */
public class RelationshipUtil {
    protected static ArrayList<ArrayList<String>> roleBasedDescriptors = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList("siblings", "brother", "sister")),
            new ArrayList<>(Arrays.asList("spouses", "husband", "wife")),
            new ArrayList<>(Arrays.asList("bioparents", "parent", "child"))
    ));
    protected static ArrayList<String> rolelessDescriptors = new ArrayList<>(
            Arrays.asList("friends"));
    private final ObservableList<Relationship> relationshipsTracker = FXCollections.observableArrayList();
    private final ObservableList<Relationship> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(relationshipsTracker);

    private class Pair {
        private UUID uuid;
        private int relationshipPairIndex;
        private Pair(UUID uuid, int relationshipPairIndex) {
            this.uuid = uuid;
            this.relationshipPairIndex = relationshipPairIndex;
        }
    }

    /**
     * Adds a new relationship to the tracker.
     * @param toAdd The relationship to be added.
     */
    public void addRelationship(Relationship toAdd) {
        relationshipsTracker.add(toAdd);
    }

    /**
     * Deletes a specific relationship from the tracker.
     * @param toDelete The relationship to be deleted.
     */
    public void deleteRelationship(Relationship toDelete) {
        relationshipsTracker.remove(toDelete);
    }

    /**
     * Checks if a specific relationship exists in the tracker.
     * @param toFind The relationship to find.
     * @return true if the relationship exists, false otherwise.
     */
    public boolean hasRelationship(Relationship toFind) {
        return !relationshipsTracker.isEmpty() && relationshipsTracker.contains(toFind);
    }

    /**
     * Checks if a specific relationship exists in the tracker.
     * @param toFind The relationship to find.
     * @return true if the relationship exists, false otherwise.
     */
    public boolean hasRelationshipWithDescriptor(Relationship toFind) {
        for (Relationship relationship : relationshipsTracker) {
            if ((relationship.getPerson1().equals(toFind.getPerson1()) && relationship.getPerson2()
                    .equals(toFind.getPerson2()))
                    || (relationship.getPerson1().equals(toFind.getPerson2()) && relationship.getPerson2()
                    .equals(toFind.getPerson1()))) {
                if (relationship.getRelationshipDescriptor().equalsIgnoreCase(toFind.getRelationshipDescriptor())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a relationship with a specific descriptor exists in the tracker.
     * @param descriptor The descriptor to find.
     * @return true if the relationship exists, false otherwise.
     */
    public boolean descriptorExists(String descriptor) {
        for (Relationship relationship : relationshipsTracker) {
            if (relationship.getRelationshipDescriptor().equals(descriptor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a relationship with a specific descriptor exists in the tracker.
     * @param descriptor The descriptor to find.
     * @return true if the relationship exists, false otherwise.
     */
    public String descriptorExistsValid(String descriptor) {
        for (String relationship : validDescriptors) {
            if (relationship.equals(descriptor)) {
                return relationship;
            }
        }
        return null;
    }

    /**
     * Retrieves a string representation of an existing relationship in the tracker.
     * @param toGet The relationship to retrieve.
     * @return A string representation of the specified relationship if it exists.
     * @throws IndexOutOfBoundsException if the relationship does not exist in the tracker.
     */
    public String getExistingRelationship(Relationship toGet) {
        for (Relationship relationship : relationshipsTracker) {
            if (relationship.equals(toGet)) {
                return relationship.toString();
            }
        }
        throw new IllegalArgumentException("Relationship does not exist.");
    }

    public static void addRolelessDescriptor(String descriptor) {
        rolelessDescriptors.add(descriptor);
    }

    /**
     * Adds a new role-based descriptor to the list of valid role-based descriptors.
     *
     * @param descriptor The descriptor to be added.
     * @param role1 The role of the first person in the relationship.
     * @param role2 The role of the second person in the relationship.
     */
    public static void addRoleBasedDescriptor(String descriptor, String role1, String role2) {
        ArrayList<String> descriptorList = new ArrayList<>();
        descriptorList.add(descriptor);
        descriptorList.add(role1);
        descriptorList.add(role2);
        roleBasedDescriptors.add(descriptorList);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Relationship> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    /**
     * Deletes all relationships associated with a person.
     * @param personUuid The UUID of the person whose relationships are to be deleted.
     */
    public void deleteRelationshipsOfPerson(UUID personUuid) {
        relationshipsTracker.removeIf(relationship -> relationship.getPerson1().equals(personUuid)
                || relationship.getPerson2().equals(personUuid));
    }

    /**
     * Replaces the contents of this list with {@code relationships}.
     * {@code persons} must not contain duplicate relationships.
     */
    public void setRelationships(List<Relationship> relationships) {
        requireAllNonNull(relationships);
        relationshipsTracker.setAll(relationships);
    }
    /**
     * Performs a breadth-first search (BFS) through the relationships tracker to find a path
     * of relationship descriptors between two UUIDs, representing the origin and target entities.
     * This method considers all types of relationships in the search.
     *
     * @param origin The UUID of the origin entity from which the search begins.
     * @param target The UUID of the target entity the search aims to find a path to.
     * @return a listcontaining the relationship descriptors in the order
     *         encountered from the origin to the target. If no path exists, returns an empty list.
     */
    public ArrayList<String> anySearchDescriptors(UUID origin, UUID target) {
        ArrayList<String> result = new ArrayList<>();
        HashSet<UUID> visited = new HashSet<>();
        Pair[] parent = new Pair[relationshipsTracker.size()];
        ArrayList<Pair> frontier = new ArrayList<>();
        frontier.add(new Pair(origin, -1));; //since we came from nowhere
        visited.add(origin);
        while (!frontier.isEmpty()) {
            ArrayList<Pair> nextFrontier = new ArrayList<>();
            for (Pair currentNode: frontier) {
                UUID start = currentNode.uuid;
                for (int i = 0; i < relationshipsTracker.size(); i++) {
                    Relationship current = relationshipsTracker.get(i);
                    UUID nextUuid = current.containsUuid(start);
                    if (nextUuid == null) {
                        continue;
                    }
                    if (nextUuid.equals(target)) {
                        parent[i] = new Pair(start, currentNode.relationshipPairIndex);
                        int currentIdx = i;
                        while (currentIdx != -1) {
                            Pair parentPair = parent[currentIdx];
                            Relationship edge = relationshipsTracker.get(currentIdx);
                            result.add(0, edge.getRelativeRelationshipDescriptor(parentPair.uuid));
                            currentIdx = parentPair.relationshipPairIndex;
                        }
                        return result;
                    }
                    if (!visited.contains(nextUuid)) {
                        visited.add(nextUuid);
                        parent[i] = new Pair(start, currentNode.relationshipPairIndex);
                        nextFrontier.add(new Pair(nextUuid, i));
                    }
                }
            }
            frontier = nextFrontier;
        }
        return result;
    }
    /**
     * Performs a breadth-first search (BFS) through the relationships tracker to find a path
     * of relationship descriptors between two UUIDs, representing the origin and target entities.
     * This method considers all types of relationships in the search.
     *
     * @param origin The UUID of the origin entity from which the search begins.
     * @param target The UUID of the target entity the search aims to find a path to.
     * @return a listcontaining the relationship descriptors in the order
     *         encountered from the origin to the target. If no path exists, returns an empty list.
     */
    public ResultContainer anySearchForTreeMap(UUID origin, UUID target) {
        ArrayList<UUID> relatedPersonsUuid = new ArrayList<>();
        ArrayList<Relationship> relationships = new ArrayList<>();
        ArrayList<String> relationshipPathwayBuilder = new ArrayList<>();
        HashSet<UUID> visited = new HashSet<>();
        Pair[] parent = new Pair[relationshipsTracker.size()];
        ArrayList<Pair> frontier = new ArrayList<>();
        frontier.add(new Pair(origin, -1));; //since we came from nowhere
        visited.add(origin);

        while (!frontier.isEmpty()) {
            ArrayList<Pair> nextFrontier = new ArrayList<>();
            for (Pair currentNode: frontier) {
                UUID start = currentNode.uuid;
                for (int i = 0; i < relationshipsTracker.size(); i++) {
                    Relationship current = relationshipsTracker.get(i);
                    UUID nextUuid = current.containsUuid(start);
                    if (nextUuid == null) {
                        continue;
                    }
                    if (nextUuid.equals(target)) {
                        relatedPersonsUuid.add(nextUuid);
                        relationshipPathwayBuilder.add(getLastFourCharacterOfUuid(nextUuid));
                        parent[i] = new Pair(start, currentNode.relationshipPairIndex);
                        int currentIdx = i;
                        while (currentIdx != -1) {
                            Pair parentPair = parent[currentIdx];
                            Relationship edge = relationshipsTracker.get(currentIdx);
                            relationships.add(edge);
                            relatedPersonsUuid.add(parentPair.uuid);
                            relationshipPathwayBuilder.add(
                                    edge.getRelativeRelationshipDescriptorWithoutUuid(parentPair.uuid));
                            relationshipPathwayBuilder.add(getLastFourCharacterOfUuid(parentPair.uuid));
                            currentIdx = parentPair.relationshipPairIndex;
                        }
                        Collections.reverse(relatedPersonsUuid);
                        Collections.reverse(relationships);
                        Collections.reverse(relationshipPathwayBuilder);
                        StringBuilder relationshipPathwayCompactor = new StringBuilder();
                        int idx = 0;
                        while (idx < relationshipPathwayBuilder.size()) {
                            if (idx == 0) {
                                relationshipPathwayCompactor.append(relationshipPathwayBuilder.get(idx));
                                idx++;
                            } else {
                                String toAdd = String.format(" --> %s --> %s", relationshipPathwayBuilder.get(idx),
                                        relationshipPathwayBuilder.get(idx + 1));
                                relationshipPathwayCompactor.append(toAdd);
                                idx += 2;
                            }
                        }
                        return new ResultContainer(relatedPersonsUuid, relationships,
                                relationshipPathwayCompactor.toString());
                    }
                    if (!visited.contains(nextUuid)) {
                        visited.add(nextUuid);
                        parent[i] = new Pair(start, currentNode.relationshipPairIndex);
                        nextFrontier.add(new Pair(nextUuid, i));
                    }
                }
            }
            frontier = nextFrontier;
        }
        return null;
    }
    /**
     * Searches for a path of family relationships between two entities identified by their UUIDs,
     * specifically considering only those relationships that are instances of FamilyRelationship.
     * Utilizes a breadth-first search (BFS) strategy to navigate through the relationships tracker.
     *
     * @param origin The UUID of the entity from which to start the search.
     * @param target The UUID of the entity to find a path to, using only family relationships.
     * @return A list listing the family relationship descriptors from the origin
     *         to the target, in order encountered. Returns an empty list if no such path exists.
     */
    public ArrayList<String> familySearchDescriptors(UUID origin, UUID target) {
        ArrayList<String> result = new ArrayList<>();
        HashSet<UUID> visited = new HashSet<>();
        Pair[] parent = new Pair[relationshipsTracker.size()];
        ArrayList<Pair> frontier = new ArrayList<>();
        frontier.add(new Pair(origin, -1));; //since we came from nowhere
        visited.add(origin);
        while (!frontier.isEmpty()) {
            ArrayList<Pair> nextFrontier = new ArrayList<>();
            for (Pair currentNode: frontier) {
                UUID start = currentNode.uuid;
                for (int i = 0; i < relationshipsTracker.size(); i++) {
                    Relationship current = relationshipsTracker.get(i);
                    if (!(current instanceof FamilyRelationship)) {
                        continue;
                    }
                    UUID nextUuid = current.containsUuid(start);
                    if (nextUuid == null) {
                        continue;
                    }
                    if (nextUuid.equals(target)) {
                        parent[i] = new Pair(start, currentNode.relationshipPairIndex);
                        int currentIdx = i;
                        while (currentIdx != -1) {
                            Pair parentPair = parent[currentIdx];
                            Relationship edge = relationshipsTracker.get(currentIdx);
                            result.add(0, edge.getRelativeRelationshipDescriptor(parentPair.uuid));
                            currentIdx = parentPair.relationshipPairIndex;
                        }
                        return result;
                    }
                    if (!visited.contains(nextUuid)) {
                        visited.add(nextUuid);
                        parent[i] = new Pair(start, currentNode.relationshipPairIndex);
                        nextFrontier.add(new Pair(nextUuid, i));
                    }
                }
            }
            frontier = nextFrontier;
        }
        return result;
    }


    /**
     * Searches for a path of family relationships between two entities identified by their UUIDs,
     * specifically considering only those relationships that are instances of FamilyRelationship.
     * Utilizes a breadth-first search (BFS) strategy to navigate through the relationships tracker.
     *
     * @param origin The UUID of the entity from which to start the search.
     * @param target The UUID of the entity to find a path to, using only family relationships.
     * @return A ResultContainer listing the family relationship descriptors from the origin
     *         to the target, in order encountered. Returns null if no such path exists.
     */
    public ResultContainer familySearchForTreeMap(UUID origin, UUID target) {
        ArrayList<UUID> relatedPersonsUuid = new ArrayList<>();
        ArrayList<Relationship> relationships = new ArrayList<>();
        ArrayList<String> relationshipPathwayBuilder = new ArrayList<>();
        HashSet<UUID> visited = new HashSet<>();
        Pair[] parent = new Pair[relationshipsTracker.size()];
        ArrayList<Pair> frontier = new ArrayList<>();
        frontier.add(new Pair(origin, -1));; //since we came from nowhere
        visited.add(origin);

        while (!frontier.isEmpty()) {
            ArrayList<Pair> nextFrontier = new ArrayList<>();
            for (Pair currentNode: frontier) {
                UUID start = currentNode.uuid;
                for (int i = 0; i < relationshipsTracker.size(); i++) {
                    Relationship current = relationshipsTracker.get(i);
                    if (!(current instanceof FamilyRelationship)) {
                        continue;
                    }
                    UUID nextUuid = current.containsUuid(start);
                    if (nextUuid == null) {
                        continue;
                    }
                    if (nextUuid.equals(target)) {
                        relatedPersonsUuid.add(nextUuid);
                        relationshipPathwayBuilder.add(getLastFourCharacterOfUuid(nextUuid));
                        parent[i] = new Pair(start, currentNode.relationshipPairIndex);
                        int currentIdx = i;
                        while (currentIdx != -1) {
                            Pair parentPair = parent[currentIdx];
                            Relationship edge = relationshipsTracker.get(currentIdx);
                            relationships.add(edge);
                            relatedPersonsUuid.add(parentPair.uuid);
                            relationshipPathwayBuilder.add(
                                    edge.getRelativeRelationshipDescriptorWithoutUuid(parentPair.uuid));
                            relationshipPathwayBuilder.add(getLastFourCharacterOfUuid(parentPair.uuid));
                            currentIdx = parentPair.relationshipPairIndex;
                        }
                        Collections.reverse(relatedPersonsUuid);
                        Collections.reverse(relationships);
                        Collections.reverse(relationshipPathwayBuilder);
                        StringBuilder relationshipPathwayCompactor = new StringBuilder();
                        int idx = 0;
                        while (idx < relationshipPathwayBuilder.size()) {
                            if (idx == 0) {
                                relationshipPathwayCompactor.append(relationshipPathwayBuilder.get(idx));
                                idx++;
                            } else {
                                String toAdd = String.format(" --> %s --> %s", relationshipPathwayBuilder.get(idx),
                                        relationshipPathwayBuilder.get(idx + 1));
                                relationshipPathwayCompactor.append(toAdd);
                                idx += 2;
                            }
                        }
                        return new ResultContainer(relatedPersonsUuid, relationships,
                                relationshipPathwayCompactor.toString());
                    }
                    if (!visited.contains(nextUuid)) {
                        visited.add(nextUuid);
                        parent[i] = new Pair(start, currentNode.relationshipPairIndex);
                        nextFrontier.add(new Pair(nextUuid, i));
                    }
                }
            }
            frontier = nextFrontier;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RelationshipUtil)) {
            return false;
        }
        RelationshipUtil other = (RelationshipUtil) o;
        return relationshipsTracker.equals(other.relationshipsTracker);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Relationship relationship : relationshipsTracker) {
            sb.append(relationship.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * util method to return last 4 character of uuid
     * @param uuid uuid to obtain the last 4 characters from
     * @return string containing the last 4 characters
     */
    private String getLastFourCharacterOfUuid(UUID uuid) {
        String uuidString = uuid.toString();
        int len = uuidString.length();
        return uuidString.substring(len - 4);
    }

    /**
     * Adds a new relationship type to the list of valid relationship types.
     */
    public void deleteRelationType(String relationType) {
        if (!validDescriptors.contains(relationType)) {
            throw new IllegalArgumentException("Relationship type does not exist yet");
        }
        if (relationType.equals("siblings") || relationType.equals("friend")
                || relationType.equals("spouses") || relationType.equals("bioparents")) {
            throw new IllegalArgumentException("Cannot delete default relationship type");
        }
        if (descriptorExists(relationType)) {
            throw new IllegalArgumentException("There are relationships under this relation type. "
                    + "\nPlease delete them first.");
        }
        validDescriptors.remove(relationType);
        if (roleBasedDescriptors.contains(relationType)) {
            roleBasedDescriptors.remove(relationType);
        }
        if (rolelessDescriptors.contains(relationType)) {
            rolelessDescriptors.remove(relationType);
        }
    }

    /**
     * Checks if a relationship type is role-based.
     * @param descriptor The descriptor to check.
     * @return true if the relationship type is role-based, false otherwise.
     */
    public boolean isRelationRoleBased(String descriptor) {
        for (ArrayList<String> relationship : roleBasedDescriptors) {
            if (relationship.get(0).equals(descriptor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a relationship type is roleless.
     * @param descriptor The descriptor to check.
     * @return true if the relationship type is roleless, false otherwise.
     */
    public boolean isRelationRoleless(String descriptor) {
        return rolelessDescriptors.contains(descriptor);
    }

    /**
     * Retrieves the roles associated with a specific relationship type.
     * @param descriptor The descriptor to retrieve roles for.
     * @return A list containing the roles associated with the specified descriptor.
     */
    public List<String> getRoles(String descriptor) {
        List<String> roles = new ArrayList<>();
        for (ArrayList<String> relationship : roleBasedDescriptors) {
            if (relationship.get(0).equals(descriptor)) {
                ArrayList<String> roleBasedRelationship = relationship;
                roles.add(roleBasedRelationship.get(1));
                roles.add(roleBasedRelationship.get(2));
            }
        }
        return roles;
    }

    /**
     * Checks if a relationship with the same roles already exists in the tracker.
     * @param relationship The relationship to check.
     * @param uuid1 The UUID of the first person in the relationship.
     * @param uuid2 The UUID of the second person in the relationship.
     * @return true if the relationship with the same roles exists, false otherwise.
     */
    public boolean hasRelationshipWithRoles(RoleBasedRelationship relationship, UUID uuid1, UUID uuid2) {
        for (Relationship storedRelationship : relationshipsTracker) {
            if (storedRelationship instanceof RoleBasedRelationship) {
                RoleBasedRelationship storedRoleBasedRelationship = (RoleBasedRelationship) storedRelationship;
                if (storedRoleBasedRelationship.equals(relationship)) {
                    String storedRole1 = storedRoleBasedRelationship.getRole(uuid1);
                    String storedRole2 = storedRoleBasedRelationship.getRole(uuid2);
                    String newRole1 = relationship.getRole(uuid1);
                    String newRole2 = relationship.getRole(uuid2);
                    if (storedRole1.equals(newRole1) && storedRole2.equals(newRole2)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<ArrayList<String>> getRoleBasedDescriptors() {
        return roleBasedDescriptors;
    }

    public ArrayList<String> getRolelessDescriptors() {
        return rolelessDescriptors;
    }

    public void setRelationshipDescriptors(ArrayList<String> rolelessDescriptors,
                                                  ArrayList<ArrayList<String>> roleBasedDescriptors) {
        this.rolelessDescriptors = rolelessDescriptors;
        this.roleBasedDescriptors = roleBasedDescriptors;
    }

    public RoleBasedRelationship getBioparentsCount(Model model, String originUuid, String targetUuid,
                                                    String role1, String role2) throws CommandException {
        RoleBasedRelationship toAdd;
        ObservableList<Relationship> allRelationships = model.getFilteredRelationshipList();
        UUID fullOriginUuid = model.getFullUuid(originUuid);
        UUID fullTargetUuid = model.getFullUuid(targetUuid);
        int originBioParentsCount = 0;
        int targetBioParentsCount = 0;
        for (Relationship r : allRelationships) {
            if (r.getRelationshipDescriptor().equalsIgnoreCase("Bioparents")
                    && (r.getPerson1().equals(fullOriginUuid) || r.getPerson2().equals(fullOriginUuid))) {
                if (r.getPerson1().equals(fullOriginUuid)) {
                    RoleBasedRelationship r1 = (RoleBasedRelationship) r;
                    if (r1.getRole(fullOriginUuid).equals("child")) {
                        originBioParentsCount++;
                    }
                } else if (r.getPerson2().equals(fullOriginUuid)) {
                    RoleBasedRelationship r2 = (RoleBasedRelationship) r;
                    if (r2.getRole(fullOriginUuid).equals("child")) {
                        originBioParentsCount++;
                    }
                }
            }
            if (r.getRelationshipDescriptor().equalsIgnoreCase("Bioparents")
                    && (r.getPerson1().equals(fullTargetUuid) || r.getPerson2().equals(fullTargetUuid))) {
                if (r.getPerson1().equals(fullTargetUuid)) {
                    RoleBasedRelationship r1 = (RoleBasedRelationship) r;
                    if (r1.getRole(fullTargetUuid).equals("child")) {
                        targetBioParentsCount++;
                    }
                } else if (r.getPerson2().equals(fullTargetUuid)) {
                    RoleBasedRelationship r2 = (RoleBasedRelationship) r;
                    if (r2.getRole(fullTargetUuid).equals("child")) {
                        targetBioParentsCount++;
                    }
                }
            }
        }
        if (originBioParentsCount >= 2) {
            throw new CommandException("Sorry, " + originUuid + " already has 2 bioparent relationships");
        }
        if (targetBioParentsCount >= 2) {
            throw new CommandException("Sorry, " + targetUuid + " already has 2 bioparent relationships");
        }
        toAdd = new BioParentsRelationship(fullOriginUuid, fullTargetUuid, role1, role2);
        return toAdd;
    }

    /**
     * Checks if the specified roles for adding a relationship between two persons are compatible with their genders,
     * specifically for the relationship types "Siblings" and "Spouses". If the roles are incompatible with the genders
     * inferred from existing relationships, it throws a CommandException indicating the mismatch.
     * If the relationship is of type "Siblings", it ensures that the roles provided for each person are consistent with
     * their genders.
     * If the relationship is of type "Spouses", it performs the same gender compatibility check as for "Siblings".
     * If the specified relationship type is "Siblings", it constructs and returns a new SiblingRelationship object
     * with the provided role information. Otherwise, it constructs and returns a new SpousesRelationship object.
     *
     * @param model The model containing the relationships.
     * @param originUuid The UUID of the first person in the relationship.
     * @param targetUuid The UUID of the second person in the relationship.
     * @param role1 The inputted role of the first person.
     * @param role2 The inputted role of the second person.
     * @param isSiblings A boolean indicating if the relationship is of type "Siblings".
     * @return A RoleBasedRelationship object representing the new relationship.
     * @throws CommandException If the roles provided for the persons are incompatible with their genders as inferred
     *                          from existing relationships.
     */
    public RoleBasedRelationship checkSiblingsSpousesGender(Model model, String originUuid, String targetUuid,
                                                            String role1, String role2,
                                                            Boolean isSiblings) throws CommandException {
        ObservableList<Relationship> allRelationships = model.getFilteredRelationshipList();
        UUID fullOriginUuid = model.getFullUuid(originUuid);
        UUID fullTargetUuid = model.getFullUuid(targetUuid);
        String genderPerson1 = null;
        String genderPerson2 = null;
        for (Relationship r : allRelationships) {
            genderPerson1 = validateGender(r, fullOriginUuid, genderPerson1);
            genderPerson2 = validateGender(r, fullTargetUuid, genderPerson2);
        }
        if (genderPerson1 != null) {
            genderError(genderPerson1, role1, originUuid);
        }
        if (genderPerson2 != null) {
            genderError(genderPerson2, role2, targetUuid);
        }
        if (isSiblings) {
            return new SiblingRelationship(fullOriginUuid, fullTargetUuid, role1, role2);
        } else {
            return new SpousesRelationship(fullOriginUuid, fullTargetUuid, role1, role2);
        }
    }

    private String validateGender(Relationship relationship, UUID fullUuid, String gender) {
        if ((relationship.getRelationshipDescriptor().equalsIgnoreCase("Siblings")
                || relationship.getRelationshipDescriptor().equalsIgnoreCase("Spouses"))
                && (relationship.getPerson1().equals(fullUuid) || relationship.getPerson2().equals(fullUuid))) {
            if (relationship.getPerson1().equals(fullUuid)) {
                RoleBasedRelationship r1 = (RoleBasedRelationship) relationship;
                return r1.getRole(fullUuid);
            } else if (relationship.getPerson2().equals(fullUuid)) {
                RoleBasedRelationship r2 = (RoleBasedRelationship) relationship;
                return r2.getRole(fullUuid);
            }
        }
        return gender;
    }

    private void genderError(String gender, String role, String uuid) throws CommandException {
        if (((gender.equals("brother") || gender.equals("husband")) && (role.equals("sister")
                || role.equals("wife"))) || ((gender.equals("sister") || gender.equals("wife"))
                && (role.equals("brother") || role.equals("husband")))) {
            throw new CommandException("Sorry, " + uuid + " has been added as " + gender + ".\nPlease "
                    + "make sure that the role you are inputting for " + uuid + " matches the gender of "
                    + gender + ".\nIf you want to change the gender of " + uuid + ", please delete the"
                    + " relationship with " + gender + ".");
        }
    }

    /**
     * Checks if the inputted gender of a person matches the gender inferred from existing relationship roles.
     *
     * @param model The model containing the relationships.
     * @param uuid  The last 4 digits of the UUID of a person.
     * @param gender The gender input of a person.
     * @throws CommandException If the roles provided for the persons are incompatible with their genders as inferred
     *                          from existing relationships.
     */
    public void genderCheck(Model model, String uuid, String gender) throws CommandException {
        UUID fulluuid = model.getFullUuid(uuid.toString());
        ObservableList<Relationship> allRelationships = model.getFilteredRelationshipList();
        String genderMatch = null;
        for (Relationship r : allRelationships) {
            if ((r.getRelationshipDescriptor().equalsIgnoreCase("Siblings")
                    || r.getRelationshipDescriptor().equalsIgnoreCase("Spouses"))
                    && (r.getPerson1().equals(fulluuid) || r.getPerson2().equals(fulluuid))) {
                genderMatch = getGenderMatch(r, fulluuid);
            }
            String message = "Sorry, " + uuid + " has been added as " + genderMatch + " in their "
                    + "relationships.\nPlease make sure that the role you are inputting for " + uuid
                    + " matches the gender of " + genderMatch + ".\nIf you want to change the gender of "
                    + uuid + ", please delete the" + " relationship with " + genderMatch + ".";
            if (genderMatch != null && (genderMatch.equals("brother") || genderMatch.equals("husband"))
                    && (gender.equalsIgnoreCase("female") || gender.equalsIgnoreCase("f"))) {
                throw new CommandException(message);
            } else if (genderMatch != null && (genderMatch.equals("sister") || genderMatch.equals("wife"))
                    && (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("m"))) {
                throw new CommandException(message);
            }
            genderMatch = null;
        }
    }

    private String getGenderMatch(Relationship relationship, UUID fullUuid) {
        if (relationship.getPerson1().equals(fullUuid)) {
            RoleBasedRelationship r1 = (RoleBasedRelationship) relationship;
            return r1.getRole(fullUuid);
        } else if (relationship.getPerson2().equals(fullUuid)) {
            RoleBasedRelationship r2 = (RoleBasedRelationship) relationship;
            return r2.getRole(fullUuid);
        }
        return null;
    }

    /**
     * Resets the relationship descriptors to their default values.
     */
    public static void resetRelationshipDescriptors() {
        rolelessDescriptors = new ArrayList<>(Arrays.asList("friend"));
        roleBasedDescriptors = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("siblings", "brother", "sister")),
                new ArrayList<>(Arrays.asList("spouses", "husband", "wife")),
                new ArrayList<>(Arrays.asList("bioparents", "parent", "child"))
        ));
    }
}
