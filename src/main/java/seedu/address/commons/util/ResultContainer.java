package seedu.address.commons.util;

import java.util.ArrayList;

import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;

public class ResultContainer {
    private ArrayList<Person> persons;
    private ArrayList<Relationship> relationships;
    private String relationshipPathway;
    public ResultContainer(ArrayList<Person> persons, ArrayList<Relationship> relationships, String relationshipPathway) {
        this.persons = persons;
        this.relationships = relationships;
        this.relationshipPathway = relationshipPathway;
    }
    public ArrayList<Person> getPersons() {
        return persons;
    }
    public ArrayList<Relationship> getRelationships() {
        return relationships;
    }
    public String getRelationshipPathway() {
        return relationshipPathway;
    }
}
