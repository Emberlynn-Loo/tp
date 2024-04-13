package seedu.address.logic.commands;

import java.util.UUID;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.model.person.relationship.RoleBasedRelationship;

/**
 * This class is responsible for parsing and executing commands to add relationships between persons.
 * It supports adding relationships with and without roles.
 */
public class AddRelationshipCommand extends Command {
    public static final String COMMAND_WORD = "addrelation";
    public static final String MESSAGE_ADD_RELATIONSHIP_SUCCESS = "Add success";
    public static final String COMMAND_WORD_SHORT = "ar";
    private String originUuid;
    private String targetUuid;
    private String relationshipDescriptor;

    private String rolePerson1;
    private String rolePerson2;
    /**
     * Constructor takes in the string arguments needed to be passed into the relationship constructor and performs
     * the addition of the relationship
     * @param originUuid
     * @param targetUuid
     * @param relationshipDescriptor
     */
    public AddRelationshipCommand(String originUuid, String targetUuid, String relationshipDescriptor) {
        this.originUuid = originUuid;
        this.targetUuid = targetUuid;
        this.relationshipDescriptor = relationshipDescriptor.toLowerCase();
    }

    /**
     * Constructor takes in the string arguments needed to be passed into the relationship constructor and performs
     * the addition of the relationship
     * @param originUuid
     * @param targetUuid
     * @param relationshipDescriptor
     * @param role1
     * @param role2
     */
    public AddRelationshipCommand(String originUuid, String targetUuid, String relationshipDescriptor,
                                  String role1, String role2) {
        this.originUuid = originUuid;
        this.targetUuid = targetUuid;
        this.relationshipDescriptor = relationshipDescriptor.toLowerCase();
        this.rolePerson1 = role1.toLowerCase();
        this.rolePerson2 = role2.toLowerCase();
    }

    /**
     * Executes the command to add a relationship between two persons
     * @param model
     * @return CommandResult
     * @throws CommandException
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        boolean isRoleBased = (rolePerson1 != null) && (rolePerson2 != null);
        UUID fullOriginUuid = model.getFullUuid(originUuid);
        UUID fullTargetUuid = model.getFullUuid(targetUuid);
        if (fullTargetUuid == null || fullOriginUuid == null) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_UUID + originUuid + " and " + targetUuid);
        }
        if (fullOriginUuid == fullTargetUuid) {
            throw new CommandException("Relationships must be between 2 different people");
        }
        try {
            if (isRoleBased) {
                RoleBasedRelationship toAdd;
                if (relationshipDescriptor.equalsIgnoreCase("Bioparents")) {
                    toAdd = model.getBioparentsCount(model, originUuid, targetUuid, rolePerson1, rolePerson2);
                } else if (relationshipDescriptor.equalsIgnoreCase("Siblings")) {
                    if (model.hasAttribute(fullOriginUuid.toString(), "Sex")) {
                        model.genderMatch(rolePerson1, fullOriginUuid.toString(), originUuid);
                    }
                    if (model.hasAttribute(fullTargetUuid.toString(), "Sex")) {
                        model.genderMatch(rolePerson2, fullTargetUuid.toString(), targetUuid);
                    }
                    toAdd = model.checkSiblingsSpousesGender(model, originUuid, targetUuid, rolePerson1,
                            rolePerson2, true);
                } else if (relationshipDescriptor.equalsIgnoreCase("Spouses")) {
                    if (model.hasAttribute(fullOriginUuid.toString(), "Sex")) {
                        model.genderMatch(rolePerson1, fullOriginUuid.toString(), originUuid);
                    }
                    if (model.hasAttribute(fullTargetUuid.toString(), "Sex")) {
                        model.genderMatch(rolePerson2, fullTargetUuid.toString(), targetUuid);
                    }
                    toAdd = model.checkSiblingsSpousesGender(model, originUuid, targetUuid, rolePerson1,
                            rolePerson2, false);
                } else if (relationshipDescriptor.equalsIgnoreCase("Friends")) {
                    throw new CommandException("Sorry, friends cannot have roles");
                } else {
                    toAdd = new RoleBasedRelationship(fullOriginUuid, fullTargetUuid,
                            relationshipDescriptor, rolePerson1, rolePerson2);
                }
                if (model.isRelationRoleless(relationshipDescriptor)) {
                    throw new CommandException(String.format("Sorry, you did not add %s as a "
                            + "role based relationship."
                            + "\nIf you want to use it, please delete the roles"
                            + "\nIf you want to make it a role based relationship, please delete the "
                            + "relationtype and add it again.", relationshipDescriptor));
                }
                if (model.isRelationRoleBased(relationshipDescriptor)) {
                    if (!(rolePerson1.equals(model.getRoles(relationshipDescriptor).get(0))
                            || rolePerson1.equals(model.getRoles(relationshipDescriptor).get(1)))
                            || !(rolePerson2.equals(model.getRoles(relationshipDescriptor).get(0))
                            || rolePerson2.equals(model.getRoles(relationshipDescriptor).get(1)))) {
                        throw new CommandException(String.format("Please use the roles you added: [%s, %s]"
                                        + "\nIf you want to change the roles, please delete the"
                                        + "\nrelationtype and add it again.",
                                model.getRoles(relationshipDescriptor).get(0),
                                model.getRoles(relationshipDescriptor).get(1)));
                    }
                }
                if (model.hasRelationshipWithDescriptor(toAdd)) {
                    String existing = model.getExistingRelationship(toAdd);
                    throw new CommandException(String.format("Sorry, %s", existing));
                }
                model.addRelationship(toAdd);
                model.addRolebasedDescriptor(relationshipDescriptor, rolePerson1, rolePerson2);
                return new CommandResult(MESSAGE_ADD_RELATIONSHIP_SUCCESS);
            }
            if (model.isRelationRoleBased(relationshipDescriptor)) {
                throw new CommandException(String.format("Sorry, you added %s as a role based relationship."
                        + "\nIf you want to use it, please use the roles you added: [%s, %s]"
                        + "\nIf you want to make it a role based relationship, please delete the"
                        + "\nrelationtype and add it again.", relationshipDescriptor,
                        model.getRoles(relationshipDescriptor).get(0),
                        model.getRoles(relationshipDescriptor).get(1)));
            }
            Relationship toAdd = new Relationship(fullOriginUuid, fullTargetUuid, relationshipDescriptor);
            if (containsIllegalDescriptors(relationshipDescriptor.toLowerCase())) {
                throw new CommandException(relationshipDescriptor
                        + " relationship requires two roles to be specified.\n"
                        + "Please specify the roles in the format: "
                        + "\naddRelation /<UUID> <role> /<UUID> <role> /" + relationshipDescriptor);
            }
            if (model.hasRelationshipWithDescriptor(toAdd)) {
                String existing = model.getExistingRelationship(toAdd);
                throw new CommandException(String.format("Sorry, %s", existing));
            }
            model.addRelationship(toAdd);
            model.addRolelessDescriptor(relationshipDescriptor);
            return new CommandResult(MESSAGE_ADD_RELATIONSHIP_SUCCESS);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddRelationshipCommand)) {
            return false;
        }
        AddRelationshipCommand other = (AddRelationshipCommand) o;
        return other.originUuid.equals(this.originUuid) && other.targetUuid.equals(targetUuid)
                && other.relationshipDescriptor.equals(this.relationshipDescriptor);
    }

    /**
     * Checks if the relationship descriptor contains illegal descriptors
     *
     * @param relationshipDescriptor The relationship descriptor
     * @return A boolean indicating whether the relationship descriptor contains illegal descriptors
     */
    public static boolean containsIllegalDescriptors(String relationshipDescriptor) {
        return relationshipDescriptor.contains("parent") || relationshipDescriptor.contains("father")
                || relationshipDescriptor.contains("mother") || relationshipDescriptor.contains("dad")
                || relationshipDescriptor.contains("mom") || relationshipDescriptor.contains("mum")
                || relationshipDescriptor.contains("son") || relationshipDescriptor.contains("daughter")
                || relationshipDescriptor.contains("child") || relationshipDescriptor.contains("offspring")
                || relationshipDescriptor.contains("kin") || relationshipDescriptor.contains("kid")
                || relationshipDescriptor.contains("bro") || relationshipDescriptor.contains("sis")
                || relationshipDescriptor.contains("husband") || relationshipDescriptor.contains("wife");
    }
}