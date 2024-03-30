package seedu.address.commons.util;

import java.util.ArrayList;
import java.util.UUID;


import seedu.address.model.person.relationship.Relationship;

public class ResultContainer {
    private ArrayList<UUID> personsUuid;
    private ArrayList<Relationship> relationships;
    private String relationshipPathway;
    public ResultContainer(ArrayList<UUID> personsUuid, ArrayList<Relationship> relationships, String relationshipPathway) {
        this.personsUuid = personsUuid;
        this.relationships = relationships;
        this.relationshipPathway = relationshipPathway;
    }
    public ArrayList<UUID> getPersons() {
        return personsUuid;
    }
    public ArrayList<Relationship> getRelationships() {
        return relationships;
    }
    public String getRelationshipPathway() {
        return relationshipPathway;
    }
}
