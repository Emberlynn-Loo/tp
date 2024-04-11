---
layout: page
title: Developer Guide
---

<h1 align="center"><i>GENE-NIE DEVELOPER GUIDE</i></h1>

## Table of Contents

- [Introduction](#introduction)
- [Acknowledgements](#acknowledgements)
- [Setting up, getting started](#setting-up-getting-started)
- [Design](#design)
    - [Architecture](#architecture)
    - [UI component](#ui-component)
    - [Logic component](#logic-component)
    - [Model component](#model-component)
        - [Model component - Person](#model-component---person)
        - [Model component - Attribute](#model-component---attribute)
        - [Model component - Relationship](#model-component---relationship)
    - [Storage component](#storage-component)
    - [Common classes](#common-classes)
- [Implementation](#implementation)
    - [Proposed Undo/redo feature](#proposed-undoredo-feature)
    - [Proposed Implementation](#proposed-implementation)
    - [Design considerations](#design-considerations)
- [Documentation, logging, testing, configuration, dev-ops](#documentation-logging-testing-configuration-dev-ops)

--------------------------------------------------------------------------------------------------------------------
## Introduction

This is the developer guide for Gene-nie, a desktop application for managing family members and their relationships. It is optimized for use via a Command Line Interface (CLI) while still having the benefits of a Graphical User Interface (GUI). If you are a developer, this guide will help you understand the architecture of Gene-nie and how to extend its features.

--------------------------------------------------------------------------------------------------------------------

## Acknowledgements

Some code generated using GitHub Copilot, where commented as such in the code.

--------------------------------------------------------------------------------------------------------------------

## Setting up, getting started

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## Design

<div markdown="span" class="alert alert-primary">

 **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 0000`, assuming that `0000` corresponds to a valid Person UUID.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

[Back to Table of Contents](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g. `StatusBarFooter`, `HelpWindow`, `DisplaySection`, `FooterButtonSection` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` and `Relationship` objects residing in the `Model`.

[Back to Table of Contents](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete /0000")` API call as an example (assuming that `0000` is a valid UUID).

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

**Note** : The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
*Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.*
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

[Back to Table of Contents](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

### Model component

**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data where
    * `Person` objects are contained in a `UniquePersonList` object.
    * `Relationship` objects are contained in a `RelationshipUtil` object.
* stores the currently 'selected' `Person` and `Relationship` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` and `ObservableList<Relationship>` respectively that can be 'observed' i.e. the UI can be bound to these lists so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

#### Model component - Person

<img src="images/PersonClassDiagram.png" width="550" />

The `Person` component,

* contains details about the person, stored as `Attribute` objects.
    * The `Person` component,
        * stores the Unique User ID or UUID of the person
        * stores any number of `Attribute` objects in the `Person` object in the hash map
        * does not require to have any `Attribute` objects
    * The `Unique User ID` component,
        * acts as the unique identifier for a person
        * the UUID is generated by the system and is unique for each person
        * it is used for identifying the person in the system whenever calls are made to the system
        * does not depend on the other components (as the `Unique User ID` is a standalone store of details about the `Person`)

#### Model component - Attribute

<img src="images/AttributeClassDiagram.png" width="550" />

* The `Attribute` component,
  * is used to store details about a `Person`
  * each attribute has a `name` and a `value`
  * stores the `Attribute` objects in the `Person` object in the hash map.
  * Has general types of attributes (`StringAttribute`, `IntegerAttribute`, `DateAttribute`) which allows for the user to instantiate their own attributes with a name and string value
    * `StringAttribute` - stores a string value
    * `IntegerAttribute` - stores an integer value
    * `DateAttribute` - stores a date value
  * Has specific types of attributes (e.g. `NameAttribute`, `PhoneNumberAttribute`) with unique constraints relevant to their validity
    * `NameAttribute` - stores the name of the person
    * `PhoneNumberAttribute` - stores the phone number of the person and must be an integer of less than 10 digits
    * `GenderAttribute` - stores the gender of the person and must be either `Male` or `Female`
  * does not depend on the other components (as the `Attribute` are standalone stores of details about the `Person`)
#### Model component - Relationship

<img src="images/RelationshipClassDiagram.png" width="550" />

The `Relationship` component,

* contains details about a relationship between two persons.
* has a `relationType` that defines the type of relationship.
* stores UUIDs of `Person` objects that are connected by the `Relationship` object.
* can be a general `Relationship` which allows the user to define their own `relationType` or a specific one that has a predefined `RelationType`. 
    * `FriendsRelationship` is a relationship class that has a predefined `relationType` of `friends`.
* can be a general `RoleBasedRelationship` which allows the user to define their own `RelationType` and roles for each Person or a specific one that has those predefined. 
    * `FamilyRelationship` is a abstract class that extends from `RoleBasedRelationship`. It is the superclass of `BioParentsRelationship`, `SpousesRelationship` and `SiblingsRelationship`.
      * `BioParentsRelationship` is a relationship class that has a predefined `relationType` of `bioparents` and has predefined roles `parent` and `child`.
      * `SpousesRelationship` is a relationship class that has a predefined `relationType` of `spouses` and has predefined roles `husband` and `wife`.
      * `SiblingRelationship` is a relationship class that has a predefined `relationType` of `siblings` and has predefined roles `brother` and `sister`.

[Back to Table of Contents](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both Gene-nie data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

[Back to Table of Contents](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

[Back to Table of Contents](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

## Implementation

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

[Back to Table of Contents](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

### Add Attribute feature

#### Implementation

The Add Attribute mechanism is facilitated by the `AddAttributeCommand` and `AddAttributeParser`.
The `AddAttributeCommand` class is responsible for adding an attribute to a person object. 
The `AddAttributeCommand` class extends the `Command` class.
Given below is an example usage scenario and how adding attributes to a person works by.

Step 1: The user enters a command to add an attribute to a person, for example: `addattribute /1234 /Phone 12345678`.

Step 2: The `LogicManager` component receives this command as a string and passes it to the `AddressBookParser`.

Step 3: The `AddressBookParser` recognizes the `addattribute` keyword and creates a new `AddAttributeParser`.

Step 4: The `AddAttributeParser` parses the rest of the command `/1234 /Phone 12345678` and creates a new `AddAttributeCommand` with the provided UUID and attribute.

Step 5: The `AddAttributeCommand` is executed.

Step 6: `AddAttributeCommand#execute` calls the following methods from `Model`:

* `Model#getFullUuid(String)` It retrieves the full UUID of the person passed into the `AddAttribute` command.
* `Model#getPersonByUuid(UUID)` It retrieves the person with the provided `UUID` from the `Model`.
* `Model#hasAttribute(String, String)` It checks whether the person given already has the attribute that we are adding. 

Step 7: If the person does not have the attribute, the `AddAttributeCommand` adds the attribute to the person object by calling the following methods from `AttributeUtil`:
* `AttributeUtil#createAttribute(String, String)` It creates a new attribute with the provided name and value.

Step 8: `AddAttributeCommand#execute` returns the `CommandResult` object to the `LogicManager` component.

The following sequence diagram shows how the `AddAttribute` shows how the `AddAttribute` command works:

![AttributeAdditionSequenceDiagram](images/AttributeAdditionSequenceDiagram.png)

:information_source: **Note:** The lifeline for `Attribute`, `Model` and `Storage` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

The following activity diagram sheds more light on exactly what happens a user executes the `addattribute` command:

![AttributeAdditionActivityDiagram](images/AddAttributeActivityDiagram.png)

#### Design considerations

**Aspect: How the attribute is created:**

* **Alternative 1 (current choice):** The `AttribueteUtil` class is responsible for creation of the object during the execution of the command.
  * Pros: Modular approach. Easy to extend and change the implementation of the attribute creation in the future.
  * Cons: Might be over engineering for a simple task.
* **Alternative 2:** The `AddAttributeCommand` class is responsible for creation of the object during the execution of the command.
  * Pros: Simple and straightforward.
  * Cons: Might lead to a bloated class.

**Aspect: Addition of existing attribute to a person:**

* **Alternative 1 (current choice):** The `Model` component checks if the person already has the attribute before adding it.
  * Pros: Ensures that the person does not have duplicate attributes.
  * Cons: Might cause performance issues if the person has a large number of attributes.
* **Alternative 2:** The `Model` class does not check if the person already has the attribute before adding it.
  * Pros: Straightforward implementation where the attribute rewrites over all existing data.
  * Cons: Might cause confusion for the user when adding an attribute that already exists.

--------------------------------------------------------------------------------------------------------------------
## Documentation, logging, testing, configuration, dev-ops

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

[Back to Table of Contents](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

## Appendix: Requirements

### Product scope

**Target user profile**:

* Has many family members to keep track off
* Wants to keep various information about these different family members
* Wants to visualize connections between family members
* Often only has provisional or partial information about their family members


**Value proposition**: Gene-nie puts connections between people at the forefront.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I want to …​                                                                         | So that I can…​                                        |
|----------|---------|--------------------------------------------------------------------------------------|--------------------------------------------------------|
| `* * *`  | user    | Create a new person with a Unique User ID                                            | Ensure every person in my address book is distinct     |
| `* * *`  | user    | Add custom attributes to each person                                                 | Store personal information relevant to each individual |
| `* * *`  | user    | Establish and record relationships between people                                    | Visualize connections between people                   |
| `* * *`  | user    | Find the relationship path between people                                            | Understand how everyone is connected                   |
| `* *`    | user    | Have clear success or failure messages after actions                                 | Ensure that the intended action has taken place        |
| `* *`    | user    | Sort and search a person by their attributes                                         | Find specific individuals more easily                  |
| `* * *`  | user    | Have my data automatically saved once i exit the app and loaded once i start the app | Seamless experience across different sessions          |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a new person**

**MSS**

1.  User requests to add a new person to Gene-nie.
2.  Gene-nie prompts user for details of new person.
3.  User enters the necessary information.
4.  Gene-nie adds person with the provided details.
5.  Gene-nie displays new person added and confirmation message.

    Use case ends.

**Extensions**

* 3a. User enters incomplete or invalid information.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

**Use case: Delete a person**

**MSS**

1.  User requests to list persons.
2.  Gene-nie displays a list of persons.
3.  User selects a specific person in the list to delete by providing the person’s UUID.
4.  Gene-nie deletes the person.
5.  Gene-nie displays confirmation message.

    Use case ends.

**Extensions**

* 2a. The list is empty.

    * 2a1. Gene-nie informs the user the list is empty.

      Use case ends.

* 3a. Given UUID is invalid or does not exist.

    * 3a1. Gene-nie shows an error message.

      Use case resumes at step 4.

**Use case: Add an attribute to a person**

**MSS**

1.  User requests to add an attribute to a person.
2.  Gene-nie prompts user to enter the person’s UUID and attribute details.
3.  User enters UUID and attribute information.
4.  Gene-nie adds attribute details to specific person’s profile.
5.  Gene-nie displays attribute in person’s profile and confirmation message.

    Use case ends.

**Extensions**

* 3a. Attribute details are invalid.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

* 3b. Given UUID is invalid or does not exist.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

**Use case: Edit to add an attribute to a person**

**MSS**

1.  User requests to edit to add an attribute to a person.
2.  Gene-nie prompts user to enter the person’s UUID and attribute details to add.
3.  User enters UUID and attribute information.
4.  Gene-nie adds attribute details to specific person’s profile.
5.  Gene-nie displays attribute in person’s profile and confirmation message.

    Use case ends.

**Extensions**

* 3a. Attribute details are invalid.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

* 3b. Given UUID is invalid or does not exist.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

**Use case: Edit to delete an attribute to a person**

**MSS**

1.  User requests to edit to delete an attribute to a person.
2.  Gene-nie prompts user to enter the person’s UUID and attribute to delete.
3.  User enters UUID and attribute information.
4.  Gene-nie deletes attribute in specific person’s profile.
5.  Gene-nie displays confirmation message.

    Use case ends.

**Extensions**

* 3a. Attribute details are invalid.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

* 3b. Given UUID is invalid or does not exist.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

**Use case: Delete a person**

**MSS**

1.  User requests to delete a person.
2.  Gene-nie prompts user for details of person.
3.  User enters person’s UUID.
4.  Gene-nie deletes person.
5.  Gene-nie displays confirmation message.

    Use case ends.

**Extensions**

* 3a. Given UUID is invalid or does not exist.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

**Use case: Find persons by attribute**

**MSS**

1.  User requests to find persons by attribute.
2.  Gene-nie prompts user for attribute and attribute value(s).
3.  User enters attribute name and attribute value(s).
4.  Gene-nie displays list of persons with each of the specified attributes.

    Use case ends.

**Extensions**

* 3a. Attribute details are invalid or does not exist.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

* 4a. The list is empty.

    * 4a1. Gene-nie informs the user the list is empty.

      Use case ends.

**Use case: Find persons by relationship to User**

**MSS**

1.  User requests to find persons by relationship.
2.  Gene-nie prompts user for relationship to User.
3.  User enters relationship type.
4.  Gene-nie displays list of persons with specified relationship to User.

    Use case ends.

**Extensions**

* 3a. Relationship is invalid or does not exist.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

**Use case: Add a relationship between two persons**

**MSS**

1.  User requests to add a relationship between two persons.
2.  Gene-nie prompts user for UUIDs of both persons and relationship type.
3.  User enters both persons UUID and relationship type.
4.  Gene-nie creates a relationship with the provided details.
5.  Gene-nie displays new relationship added under both persons’ profiles and confirmation message.

    Use case ends.

**Extensions**

* 3a. Relationship type is invalid.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

* 3b. Given UUIDs are invalid or does not exist.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

**Use case: Edit a relationship between two persons**

**MSS**

1.  User requests to edit a specific relationship between two persons.
2.  Gene-nie prompts user for UUIDs of both persons and previous and new relationship type.
3.  User enters both persons UUID and previous and new relationship type.
4.  Gene-nie edits the specified relationship with the provided details.
5.  Gene-nie displays edited relationship under both persons’ profiles and confirmation message.

    Use case ends.

**Extensions**

* 3a. Relationship types are invalid.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

* 3b. Given UUIDs are invalid or does not exist.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

**Use case: Delete a relationship between two persons**

**MSS**

1.  User requests to delete a specific relationship between two persons.
2.  Gene-nie prompts user for UUIDs of both persons and relationship type.
3.  User enters both persons UUID and specified relationship type.
4.  Gene-nie deletes specified relationship.
5.  Gene-nie displays confirmation message.

    Use case ends.

**Extensions**

* 3a. Relationship type is invalid.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

* 3b. Given UUIDs are invalid or does not exist.

    * 3a1. Gene-nie shows an error message.

    * 3a2. Gene-nie prompts User to enter the correct and complete information.

      Steps 3a1-3a2 are repeated until the data entered are correct.

      Use case resumes from step 4.

      Use case ends.

**Use case: Exit Gene-nie**

**MSS**

1.  User requests to exit Gene-nie.
2.  Gene-nie displays goodbye message.
3.  Gene-nie saves current data.
4.  Gene-nie closes the application window.

    Use case ends.

*{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 persons and relationships without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  User actions should prompt responses within 5 seconds, maintaining acceptable performance standards.
5.  The user interface must be intuitive for users of varying technical proficiency levels, enhancing usability.
6.  The codebase should be well-documented and maintainable to facilitate future updates and enhancements.
7.  Comprehensive documentation should be provided for developers, administrators, and end-users, supporting ease of use.
8.  The system should be able to handle data corruption or loss gracefully, minimizing the impact on users.
9.  The system architecture should support future extensions or customizations, allowing for easy addition of new features.
10. Interoperability with other systems should be supported through standard protocols and formats, promoting seamless integration.
11. The system should be designed with testability in mind, allowing for comprehensive testing of all components, features, and scenarios.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder
   2. Double-click the jar file. <br> If nothing happens after double-clicking the jar file, run `java -jar Gene-nie.jar` in the folder containing the jar file. <br>
      **Expected Outcome**: Shows the GUI with a set of sample contacts. The window size may not be optimum.


2. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location.
    2. Close the window.
    3. Re-launch the app by double-clicking the jar file.<br>
        **Expected Outcome**: The most recent window size and location is retained.


### Listing all persons
**Prerequisites**: There should be at least one person in Gene-nie.
1. Listing all persons:
    1. **Test case:** `list` <br>
       **Expected Outcome:** A list of all persons with their details is shown.


### Help
1. Seeking help:
    1. **Test case:** `help` <br>
       **Expected Outcome:** A new window is opened with a link to the help page.


### Adding a person
1. Adding a person with no attributes:
    1. **Test case:** `add` <br>
       **Expected Outcome:** A new person with a random UUID shown on the left panel of the PersonCard is added to Gene-nie. Attributes panel displays "No attributes found".

   
2. Adding a person with one attribute:
    1. **Test case:** `add /Name John` <br>
       **Expected Outcome:** A new person with a random UUID shown on the left panel of the PersonCard is added to Gene-nie. Attributes panel displays the attribute added.


3. Adding a person with multiple attributes:
    1. **Test case:** `add /Name John /Phone 98765432 /Email John@example.com` <br>
       **Expected Outcome:** A new person with a random UUID shown on the left panel of the PersonCard is added to Gene-nie. Attributes panel displays all the attributes added.


4. Adding a person with duplicate attributes:
    1. **Test case:** `add /Name John /Name Chad` <br>
       **Expected Outcome:** The last attribute value is taken. A new person with a random UUID shown on the left panel of the PersonCard is added to Gene-nie. Attributes panel displays "Name: Chad".


5. Adding a person with an invalid attribute:
    1. **Test case:** `add /Name` <br>
       **Expected Outcome:** Error message highlighted in red is shown. The command format and an example is also shown in the error message.
    2. **Test case:** `add /Sex dhdkag` <br>
       **Expected Outcome:** Error message highlighted in red is shown. It reads "Sex must only be male or female for Sex."


### Deleting a person
**Prerequisites**: There should be at least one person in Gene-nie.

1. Deleting a person with a valid uuid:
    1. **Additional Prerequisites**: Replace the UUID in the test case with a valid UUID (e.g. 0001) from your list of persons. <br>
       **Test case:** `delete /UUID` <br>
       **Expected Outcome:** The person with the given UUID is removed from Gene-nie. Details of the deleted person is shown in the status message.


2. Deleting a person with an invalid uuid:
    1. **Test case:** `delete /!!!!` <br>
       **Expected Outcome:** No person is deleted. An error message is shown, indicating that the UUID provided is invalid.


3. Deleting a person with no uuid provided:
    1. **Test case:** `delete` <br>
       **Expected Outcome:** No person is deleted. An error message is shown, indicating that the command format is invalid.


### Adding attributes to existing persons
**Prerequisites**: There should be at least one person in Gene-nie.

1. Adding an attribute to a person with a valid uuid:
    1. **Additional Prerequisites**: Replace the UUID in the test case with a valid UUID (e.g. 0001) from your list of persons. <br>
       **Test case:** `addattribute /UUID /Phone 12345678` <br>
       **Expected Outcome:** The attribute is added to the person with the given UUID. The new attribute is shown in the attributes panel of the person.


2. Adding multiple attributes to a person with a valid uuid:\
    1. **Additional Prerequisites**: Replace the UUID in the test case with a valid UUID (e.g. 0001) from your list of persons. <br>
       **Test case:** `addattribute /UUID /Phone 12345678 /Email john@example.com` <br>
       **Expected Outcome:** The attributes are added to the person with the given UUID. The new attributes are shown in the attributes panel of the person.


3. Adding an attribute to a person with an invalid uuid:
    1. **Test case:** `addattribute /!!!! /Phone 12345678` <br>
       **Expected Outcome:** No attribute is added. An error message is shown, saying "Person not found."


4. Adding an attribute with no uuid provided:
    1. **Test case:** `addattribute /Phone 12345678` <br>
       **Expected Outcome:** No attribute is added. An error message is shown, indicating that the command format is invalid.


5. Adding a duplicate attribute to a person:
    1. **Additional Prerequisites**: Replace the UUID in the test case with a valid UUID (e.g. 0001) from your list of persons. <br>
       **Test case:** `addattribute /UUID /Phone 12345678 /Phone 87654321` <br>
       **Expected Outcome:** No attribute is added. An error message is shown, indicating that the attribute is a duplicate.


### Editing attributes of existing persons
**Prerequisites**: There should be at least one person in Gene-nie, and that person should have at least one attribute.

1. Editing an attribute of a person with a valid uuid and valid attribute:
    1. **Additional Prerequisites**: Replace the UUID in the test case with a valid UUID (e.g. 0001) from your list of persons. Person must also have the attribute `Phone`. <br>
       **Test case:** `editattribute /UUID /Phone 12345678` <br>
       **Expected Outcome:** The attribute is edited for the person with the given UUID. The new attribute is shown in the attributes panel of the person.


2. Editing an attribute of a person with an invalid uuid:
    1. **Test case:** `editattribute /!!!! /Phone 12345678` <br>
       **Expected Outcome:** No attribute is edited. An error message is shown, saying "Person not found."


3. Editing an attribute of a person without the attribute provided:
    1. **Additional Prerequisites**: Replace the UUID in the test case with a valid UUID (e.g. 0001) from your list of persons. The person must NOT have the attribute `Email`. <br>
       **Test case:** `editattribute /UUID /Email john@example.com` <br>
       **Expected Outcome:** No attribute is edited. An error message is shown, indicating that the attribute does not exist and cannot be edited.


4. Editing an attribute of a person with no uuid provided:
    1. **Test case:** `editattribute /Phone 12345678` <br>
       **Expected Outcome:** No attribute is edited. An error message is shown, indicating that the command format is invalid.


### Deleting attributes of existing persons
**Prerequisites**: There should be at least one person in Gene-nie, and that person should have at least one attribute.

1. Deleting an attribute of a person with a valid uuid and valid attribute:
    1. **Additional Prerequisites**: Replace the UUID in the test case with a valid UUID (e.g. 0001) from your list of persons. Person must also have the attribute `Phone`. <br>
       **Test case:** `deleteattribute /UUID /Phone` <br>
       **Expected Outcome:** The attribute is deleted for the person with the given UUID. The attribute is removed from the attributes panel of the person.


2. Deleting multiple attributes of a person with a valid uuid and valid attributes:
    1. **Additional Prerequisites**: Replace the UUID in the test case with a valid UUID (e.g. 0001) from your list of persons. Person must also have the attributes `Phone` and `Email`. <br>
       **Test case:** `deleteattribute /UUID /Phone /Email` <br>
       **Expected Outcome:** The attributes are deleted for the person with the given UUID. The attributes are removed from the attributes panel of the person.


3. Deleting an attribute of a person with an invalid uuid:
    1. **Test case:** `deleteattribute /!!!! /Phone` <br>
       **Expected Outcome:** No attribute is deleted. An error message is shown, saying that the UUID provided is invalid.


4. Deleting an attribute of a person without the attribute provided:
    1. **Additional Prerequisites**: Replace the UUID in the test case with a valid UUID (e.g. 0001) from your list of persons. The person must NOT have the attribute `Email`. <br>
       **Test case:** `deleteattribute /UUID /Email` <br>
       **Expected Outcome:** No attribute is deleted. An error message is shown, indicating that the attribute is not found.


### Finding persons
**Prerequisites**: There should be at least one person in Gene-nie.

1. Finding persons by attribute name:
    1. **Additional Prerequisites**: The persons should have the attribute `Name`. <br>
       **Test case:** `find /Name` <br>
       **Expected Outcome:** A list of persons with the specified attribute name `Name` is shown.


2. Finding persons by attribute value:
    1. **Additional Prerequisites**: The persons should have an attribute with the value `John`. <br>
       **Test case:** `find /John` <br>
       **Expected Outcome:** A list of persons with the specified attribute containing the value `John` is shown.


3. Finding persons by uuid:
    1. **Additional Prerequisites**: Replace the UUID in the test case with a valid UUID (e.g. 0001) from your list of persons. <br>
       **Test case:** `find /UUID` <br>
       **Expected Outcome:** The person with the specified UUID is shown.


4. Finding persons using an attribute name/value that does not exist:
    1. **Additional Prerequisites**: All persons do not have the attribute `Country` and do not have any attribute with the value `Singapore`. <br>
       **Test case:** `find /Country` <br>
       **Expected Outcome:** The left panel is empty, with no person found.
    2. **Test case:** `find /Singapore` <br>
       **Expected Outcome:** The left panel is empty, with no person found.


5. Finding persons using an invalid uuid:
    1. **Test case:** `find /!!!!` <br>
       **Expected Outcome:** The left panel is empty, with no person found.


### Listing all valid relationship types

1. Listing all valid relationship types:
    1. **Test case:** `listrelations` <br>
       **Expected Outcome:** A list of all valid relationship types is shown.


### Adding relationships between persons
**Prerequisites**: There should be at least two persons in Gene-nie.

1. Adding a roleless relationship between two persons with valid uuids:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. <br>
       **Test case:** `addrelation /UUID1 /UUID2 /friends` <br>
       **Expected Outcome:** A relationship of type `friends` is added between the two persons with the given UUIDs. The relationship is shown in the relationships panel of both persons.


2. Adding a role-based relationship between two persons with valid uuids:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. <br>
       **Test case:** `addrelation /UUID1 husband /UUID2 wife /spouses` <br>
       **Expected Outcome:** A relationship of type `spouses` is added between the two persons with the given UUIDs. The relationship is shown in the relationships panel of both persons, each with their respective role.


3. Adding a relationship between two persons with an invalid uuid:
    1. **Test case:** `addrelation /!!!! /UUID2 /friends` <br>
       **Expected Outcome:** No relationship is added. An error message is shown, indicating that the UUID provided is invalid.


4. Adding a relationship between two persons with no relationship type provided:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. <br>
       **Test case:** `addrelation /UUID1 /UUID2` <br>
       **Expected Outcome:** No relationship is added. An error message is shown, indicating that the command format is invalid.


5. Adding a relationship between two persons with an invalid relationship type:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. <br>
       **Test case:** `addrelation /UUID1 /UUID2 /!!!!` <br>
       **Expected Outcome:** No relationship is added. An error message is shown, indicating that the relationship type provided is invalid.


### Editing relationships between persons
**Prerequisites**: There should be at least two persons in Gene-nie, and they should have at least one relationship between them.

1. Editing a relationship between two persons with valid uuids and valid relationship type from a roleless relationship to a roleless relationship:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. The persons must have a relationship of type `friends`. <br>
       **Test case:** `editrelation /UUID1 /UUID2 /friends /colleagues` <br>
       **Expected Outcome:** The relationship type is edited for the two persons with the given UUIDs. The relationship is shown in the relationships panel of both persons with the new relationship type.


2. Editing a relationship between two persons with valid uuids and valid relationship type from a role-based relationship to a role-based relationship:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. The persons must have a relationship of type `siblings` with roles `brother` and `sister` respectively. <br>
       **Test case:** `editrelation /UUID1 husband /UUID2 wife /siblings /spouses` <br>
       **Expected Outcome:** The relationship type is edited for the two persons with the given UUIDs. The relationship is shown in the relationships panel of both persons with the new relationship type and roles.


3. Editing a relationship between two persons with an invalid uuid:
    1. **Test case:** `editrelation /!!!! /UUID2 /friends /colleagues` <br>
       **Expected Outcome:** No relationship is edited. An error message is shown, indicating that the UUID provided is invalid.


4. Editing a relationship between two persons with an invalid relationship type:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. <br>
       **Test case:** `editrelation /UUID1 /UUID2 /friends /!!!!` <br>
       **Expected Outcome:** No relationship is edited. An error message is shown, indicating that the relationship type provided is invalid.


5. Editing a relationship between two persons with the old relationship type not present:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. The persons must NOT have a relationship of type `colleagues`. <br>
       **Test case:** `editrelation /UUID1 /UUID2 /colleagues /friends` <br>
       **Expected Outcome:** No relationship is edited. An error message is shown, indicating that the relationship `colleagues` does not exist.


### Deleting relationships between persons
**Prerequisites**: There should be at least two persons in Gene-nie, and they should have at least one relationship between them.

1. Deleting a relationship between two persons with valid uuids and valid relationship type:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. The persons must have a relationship of type `friends`. <br>
       **Test case:** `deleterelation /UUID1 /UUID2 /friends` <br>
       **Expected Outcome:** The relationship is deleted for the two persons with the given UUIDs. The relationship is removed from the relationships panel of both persons.


2. Deleting a relationship between two persons with an invalid uuid:
    1. **Test case:** `deleterelation /!!!! /UUID2 /friends` <br>
       **Expected Outcome:** No relationship is deleted. An error message is shown, indicating that the UUID provided is invalid.


3. Deleting a relationship between two persons without the relationship present:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. The persons must NOT have a relationship of type `colleagues`. <br>
       **Test case:** `deleterelation /UUID1 /UUID2 /colleagues` <br>
       **Expected Outcome:** No relationship is deleted. An error message is shown, indicating that the relationship `colleagues` does not exist.


4. Deleting a relationship between two persons with no relationship type provided:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. <br>
       **Test case:** `deleterelation /UUID1 /UUID2` <br>
       **Expected Outcome:** No relationship is deleted. An error message is shown, indicating that the command format is invalid.


### Finding the relationship path between two persons
**Prerequisites**: There should be at least two persons in Gene-nie, and they should have at least one relationship between them.

1. Finding the general relationship path between two persons with valid uuids:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. The persons must have at least one relationship between them, or be indirectly related through multiple relationships. <br>
       **Test case:** `anySearch /UUID1 /UUID2` <br>
       **Expected Outcome:** The relationship path between the two persons with the given UUIDs is shown.


2. Finding the family relationship path between two persons with valid uuids:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. The persons must have at least one family relationship between them, or be indirectly related through multiple family relationships. These family relationships types include "bioparents", "spouses" and "siblings". <br>
       **Test case:** `familySearch /UUID1 /UUID2` <br>
       **Expected Outcome:** The family relationship path between the two persons with the given UUIDs is shown.


3. Finding the family relationship path between two persons with no valid path between them:
    1. **Additional Prerequisites**: Replace the UUIDs in the test case with valid UUIDs (e.g. 0001 and 0002) from your list of persons. The persons must NOT have a valid relationship path between them. <br>
       **Test case:** `anySearch /UUID1 /UUID2` <br>
       **Expected Outcome:** The left panel is empty, with no relationship path found.


4. Finding the relationship path between two persons with a uuid that does not exist in Gene-nie:
    1. **Test case:** `anySearch /!!!! /UUID2` <br>
       **Expected Outcome:** An error message is shown, saying "You have not added the contacts of the people you are looking for!"


### Deleting all persons
**Prerequisites**: There should be at least one person in Gene-nie.

1. Deleting all persons:
    1. **Test case:** `deleteallpersons` <br>
       **Expected Outcome:** All persons are deleted. The left panel is empty, with no persons found.


### Clearing command responses

1. Clearing the command responses:
    1. **Test case:** `clear` <br>
       **Expected Outcome:** All command responses are cleared from the command box.


### Issues with saving data

1. Dealing with missing data file: <br>

   **Test case:** Remove the data file to simulate a missing file
    1. Go to the location of the data file indicated in the bottom left corner of the application.
    2. Delete the file named `addressbook.json`.
    3. Relaunch Gene-nie. <br>
       **Expected Outcome:** A new file with sample person data is created. Sample person data is shown in the application.


2. Dealing with corrupted file: <br>
   **Test case:** Modify the data file to simulate a corrupted data file
    1. Go to the location of the data file indicated in the bottom left corner of the application.
    2. Open the file named `addressbook.json`.
    3. Modify the file. For example, you could remove the first few lines of the file.
    4. Relaunch Gene-nie. <br>
       **Expected Outcome:** The left panel is now empty. No person records are shown in the application. 

