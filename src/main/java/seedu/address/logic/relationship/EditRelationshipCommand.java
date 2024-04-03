package seedu.address.logic.relationship;

import java.util.UUID;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.relationship.BioParentsRelationship;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.model.person.relationship.RoleBasedRelationship;
import seedu.address.model.person.relationship.SiblingRelationship;
import seedu.address.model.person.relationship.SpousesRelationship;

/**
 * This class is responsible for parsing and executing commands to add relationships between persons.
 * It supports adding relationships with and without roles.
 */
public class EditRelationshipCommand extends Command {
    public static final String COMMAND_WORD = "editrelation";
    public static final String MESSAGE_EDIT_RELATIONSHIP_SUCCESS = "Edit successful";
    public static final String COMMAND_WORD_SHORT = "er";
    private String originUuid;
    private String targetUuid;
    private String oldRelationshipDescriptor;
    private String newRelationshipDescriptor;

    private String role1;
    private String role2;

    /**
     * Constructor takes in the string arguments needed to be passed into the relationship constructor and performs
     * the addition of the relationship
     * @param originUuid
     * @param targetUuid
     * @param oldRelationshipDescriptor
     * @param newRelationshipDescriptor
     */
    public EditRelationshipCommand(String originUuid, String targetUuid,
                                   String oldRelationshipDescriptor, String newRelationshipDescriptor) {
        this.originUuid = originUuid;
        this.targetUuid = targetUuid;
        this.oldRelationshipDescriptor = oldRelationshipDescriptor.toLowerCase();
        this.newRelationshipDescriptor = newRelationshipDescriptor.toLowerCase();
    }

    /**
     * Constructor takes in the string arguments needed to be passed into the relationship constructor and performs
     * the addition of the relationship
     * @param originUuid
     * @param targetUuid
     * @param oldRelationshipDescriptor
     * @param newRelationshipDescriptor
     * @param role1
     * @param role2
     */
    public EditRelationshipCommand(String originUuid, String targetUuid,
                                   String oldRelationshipDescriptor, String newRelationshipDescriptor,
                                   String role1, String role2) {
        this.originUuid = originUuid;
        this.targetUuid = targetUuid;
        this.oldRelationshipDescriptor = oldRelationshipDescriptor.toLowerCase();
        this.newRelationshipDescriptor = newRelationshipDescriptor.toLowerCase();
        this.role1 = role1.toLowerCase();
        this.role2 = role2.toLowerCase();
    }

    /**
     * Executes the command to edit a relationship between two persons.
     * @param model {@code Model} which the command should operate on.
     * @return a CommandResult that contains the success message.
     * @throws CommandException if relationship does not exist or if the new relationship is the same as the old one.
     */
    public CommandResult execute(Model model) throws CommandException {
        if (originUuid == null || targetUuid == null) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_UUID);
        }
        UUID fullOriginUuid = model.getFullUuid(originUuid);
        UUID fullTargetUuid = model.getFullUuid(targetUuid);
        try {
            Relationship toEditOff = new Relationship(fullOriginUuid, fullTargetUuid, oldRelationshipDescriptor);
            Relationship toEditIn = new Relationship(fullOriginUuid, fullTargetUuid, newRelationshipDescriptor);
            if (!model.hasRelationshipWithDescriptor(toEditOff)) {
                throw new CommandException(String.format("Sorry %s do not exist", toEditOff));
            }
            model.deleteRelationship(toEditOff);
            if (model.hasRelationshipWithDescriptor(toEditIn)) {
                String existing = model.getExistingRelationship(toEditIn);
                throw new CommandException(String.format("Sorry, %s", existing));
            }
            if (role1 != null && role2 != null) {
                RoleBasedRelationship toAdd;
                if (newRelationshipDescriptor.equalsIgnoreCase("Bioparents")) {
                    toAdd = new BioParentsRelationship(fullOriginUuid, fullTargetUuid, role1, role2);
                } else if (newRelationshipDescriptor.equalsIgnoreCase("Siblings")) {
                    toAdd = new SiblingRelationship(fullOriginUuid, fullTargetUuid, role1, role2);
                } else if (newRelationshipDescriptor.equalsIgnoreCase("Spouses")) {
                    toAdd = new SpousesRelationship(fullOriginUuid, fullTargetUuid, role1, role2);
                } else if (newRelationshipDescriptor.equalsIgnoreCase("Friends")) {
                    throw new CommandException("Sorry, friends cannot have roles");
                } else {
                    toAdd = new RoleBasedRelationship(fullOriginUuid, fullTargetUuid,
                            newRelationshipDescriptor, role1, role2);
                }
                if (!model.isRelationRoleBased(newRelationshipDescriptor) && role1 == null && role2 == null) {
                    throw new CommandException(String.format("Sorry, you did not add %s as a "
                            + "role based relationship."
                            + "\nIf you want to use it, please delete the roles"
                            + "\nIf you want to make it a role based relationship, please delete the "
                            + "relationtype and add it again.", newRelationshipDescriptor));
                }
                if (model.hasDescriptor(newRelationshipDescriptor)) {
                    if ((!role1.equals(model.getRoles(newRelationshipDescriptor).get(0))
                            || !role1.equals(model.getRoles(newRelationshipDescriptor).get(1)))
                        && (!role2.equals(model.getRoles(newRelationshipDescriptor).get(0))
                            || !role2.equals(model.getRoles(newRelationshipDescriptor).get(1)))) {
                        throw new CommandException(String.format("Please use the roles you added: [%s, %s]"
                                        + "\nIf you want to make change the roles, please delete the"
                                        + "\nrelationtype and add it again.",
                                model.getRoles(newRelationshipDescriptor).get(0),
                                model.getRoles(newRelationshipDescriptor).get(1)));
                    }
                }
                model.addRelationship(toAdd);
                model.addRolebasedDescriptor(newRelationshipDescriptor);
            } else {
                if (model.isRelationRoleBased(newRelationshipDescriptor) && role1 == null && role2 == null) {
                    throw new CommandException(String.format("Sorry, you added %s as a role based relationship."
                                    + "\nIf you want to use it, please use the roles you added: [%s, %s]"
                                    + "\nIf you want to make it a role based relationship, please delete the"
                                    + "\nrelationtype and add it again.", newRelationshipDescriptor,
                            model.getRoles(newRelationshipDescriptor).get(0),
                            model.getRoles(newRelationshipDescriptor).get(1)));
                }
                model.addRelationship(toEditIn);
                model.addRolelessDescriptor(newRelationshipDescriptor);
            }
            return new CommandResult(MESSAGE_EDIT_RELATIONSHIP_SUCCESS);
        } catch (IllegalArgumentException e) {
            throw new CommandException(String.format(e.getMessage()));
        }
    }
}
