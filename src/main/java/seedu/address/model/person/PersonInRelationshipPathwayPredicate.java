package seedu.address.model.person;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class PersonInRelationshipPathwayPredicate implements Predicate<Person> {
    private final ArrayList<UUID> personUuids;

    public PersonInRelationshipPathwayPredicate(ArrayList<UUID> personUuids) {
        this.personUuids = personUuids;
    }

    /**
     * Tests that a {@code Person}'s {@code UUID} matches any of the UUIDs given.
     */
    @Override
    public boolean test(Person person) {
        return personUuids.stream().anyMatch(uuid ->
                person.getUuid().equals(uuid));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonInRelationshipPathwayPredicate)) {
            return false;
        }

        PersonInRelationshipPathwayPredicate otherPersonInRelationshipPathwayPredicate =
                (PersonInRelationshipPathwayPredicate) other;
        return personUuids.equals(otherPersonInRelationshipPathwayPredicate.personUuids);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", personUuids).toString();
    }
}

