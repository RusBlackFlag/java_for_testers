package tests;

import model.ContactData;
import model.GroupData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

public class DeleteContactTest extends TestBase {
//  @Test
//  public void deleteContactTest() {
//    app.contacts().deleteContact();
//  }

  @Test
  public void deleteContactTest() {
    if (app.jdbc().getContactList().isEmpty()) {
      app.contacts().createContact(new ContactData().withFirstname("Temp").withLastname("Contact"));
    }
    var oldContacts = app.jdbc().getContactList();

    var rnd = new Random();
    var index = rnd.nextInt(oldContacts.size());

    app.contacts().deleteContact(index);

    var newContacts = app.jdbc().getContactList();

    var expectedList = new ArrayList<>(oldContacts);
    expectedList.remove(index);

    expectedList.sort((a, b) -> (a.firstname() + a.lastname()).compareTo(b.firstname() + b.lastname()));
    newContacts.sort((a, b) -> (a.firstname() + a.lastname()).compareTo(b.firstname() + b.lastname()));

    Assertions.assertEquals(expectedList, newContacts);
  }

    @Test
    public void canRemoveContactFromGroup() {
        // предусловия
        if (app.jdbc().getGroupList().isEmpty()) {
            app.groups().createGroup(new GroupData().withName("RemovableGroup"));
        }
        if (app.jdbc().getContactList().isEmpty()) {
            app.contacts().createContact(new ContactData().withFirstname("Removable"));
        }

        var group = app.jdbc().getGroupList().get(0);
        var allContacts = app.jdbc().getContactList();
        var relatedBefore = app.jdbc().getContactsInGroup(group);

        // если в группе нет контактов — добавим один
        ContactData contact;
        if (relatedBefore.isEmpty()) {
            contact = allContacts.get(0);
            app.contacts().addContactToGroup(contact, group);
            relatedBefore = app.jdbc().getContactsInGroup(group);
        } else {
            contact = relatedBefore.get(0);
        }

        // удаляем контакт из группы через UI
        app.contacts().removeContactFromGroup(contact, group);

        // проверка через DB
        var relatedAfter = app.jdbc().getContactsInGroup(group);
        Assertions.assertEquals(relatedBefore.size() - 1, relatedAfter.size());
        Assertions.assertTrue(relatedAfter.stream().noneMatch(c -> c.id().equals(contact.id())));
    }

}
