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
        checkUuid(fullOriginUuid, fullTargetUuid);
        try {
            if (isRoleBased) {
                RoleBasedRelationship toAdd = model.getRelationshipRoleBased(fullOriginUuid, fullTargetUuid, model,
                        originUuid, targetUuid, rolePerson1, rolePerson2, relationshipDescriptor);
                model.validateRoleBasedRelation(rolePerson1, rolePerson2, relationshipDescriptor);
                model.relationshipChecks(toAdd, fullOriginUuid, fullTargetUuid, originUuid, targetUuid, rolePerson1,
                        rolePerson2, model, null, relationshipDescriptor, true);
                model.addRelationship(toAdd);
                model.addRolebasedDescriptor(relationshipDescriptor, rolePerson1, rolePerson2);
                return new CommandResult(MESSAGE_ADD_RELATIONSHIP_SUCCESS);
            }
            model.validateRoleless(rolePerson1, rolePerson2, relationshipDescriptor);
            Relationship toAdd = new Relationship(fullOriginUuid, fullTargetUuid, relationshipDescriptor);
            checkRoleless(model, toAdd);
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
        return relationshipDescriptor.equals("parent") || relationshipDescriptor.equals("father")
                || relationshipDescriptor.equals("mother") || relationshipDescriptor.equals("dad")
                || relationshipDescriptor.equals("mom") || relationshipDescriptor.equals("mum")
                || relationshipDescriptor.equals("son") || relationshipDescriptor.equals("daughter")
                || relationshipDescriptor.equals("child") || relationshipDescriptor.equals("offspring")
                || relationshipDescriptor.equals("kin") || relationshipDescriptor.equals("kid")
                || relationshipDescriptor.equals("bro") || relationshipDescriptor.equals("sis")
                || relationshipDescriptor.equals("husband") || relationshipDescriptor.equals("wife");
    }

    /**
     * Checks if the relationship descriptor contains illegal descriptors
     *
     * @param model The model
     * @param toAdd The relationship to add
     * @throws CommandException If the relationship descriptor contains illegal descriptors
     */
    public void checkRoleless(Model model, Relationship toAdd) throws CommandException {
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
    }

    /**
     * Checks if the UUIDs are valid
     *
     * @param fullOriginUuid The full origin UUID
     * @param fullTargetUuid The full target UUID
     * @throws CommandException If the UUIDs are invalid
     */
    public void checkUuid(UUID fullOriginUuid, UUID fullTargetUuid) throws CommandException {
        if (fullTargetUuid == null || fullOriginUuid == null) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_UUID + originUuid + " and " + targetUuid);
        }
        if (fullOriginUuid == fullTargetUuid) {
            throw new CommandException("Relationships must be between 2 different people");
        }
    }
}
