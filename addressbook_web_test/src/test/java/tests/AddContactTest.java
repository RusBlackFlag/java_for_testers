package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.CommonFunctions;
import model.ContactData;
import model.GroupData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AddContactTest extends TestBase {

  public static List<ContactData> contactProvider() throws IOException {
    var result = new ArrayList<ContactData>();
//    result.add(new ContactData("Test1", "Test2", "Test3", "ItsNickName"));
//    result.add(new ContactData("", "Second", "Third", "Nick"));

//    for (int i = 1; i <= 5; i++) {
//      result.add(new ContactData(
//              randomString(i * 5),
//              randomString(i * 5),
//              randomString(i * 5),
//              randomString(i * 5)
//      ));
//    }
    var json = Files.readString(Paths.get("contacts.json"));
    ObjectMapper mapper = new ObjectMapper();
    var value = mapper.readValue(json, new TypeReference<List<ContactData>>() {});
    result.addAll(value);
    return result;
  }

  @ParameterizedTest
  @MethodSource("contactProvider")
  public void createContactTest(ContactData contact) {
    app.contacts().createContact(contact);
  }


  @Test
  public void createContactTest() {
    var oldContacts = app.jdbc().getContactList();

    var newContact = new ContactData()
            .withFirstname("John")
            .withMiddlename("Middle")
            .withLastname("Doe")
            .withNickname("JDoe");
//            .withPhoto("src/test/resources/images/avatar.png");

    app.contacts().createContact(newContact);
    var newContacts = app.jdbc().getContactList();

    // находим максимальный id — это id созданного контакта
    var maxId = newContacts.stream()
            .mapToInt(c -> Integer.parseInt(c.id()))
            .max()
            .orElseThrow();

    // ожидаемый список: старые + новый с подставленным id
    var expectedList = new ArrayList<>(oldContacts);
    expectedList.add(newContact.withId(String.valueOf(maxId)));

    // сортируем по id, чтобы сравнение было корректным
    expectedList.sort((a, b) -> Integer.compare(Integer.parseInt(a.id()), Integer.parseInt(b.id())));
    newContacts.sort((a, b) -> Integer.compare(Integer.parseInt(a.id()), Integer.parseInt(b.id())));

    // сравниваем списки
    Assertions.assertEquals(expectedList, newContacts);
  }

  @Test
  public void canCreateContact() {
    var contact = new ContactData()
            .withFirstname(CommonFunctions.randomString(10))
            .withLastname(CommonFunctions.randomString(10))
            .withPhoto(randomFile("src/test/resources/images"));
    app.contacts().createContact(contact);
  }

    @Test
    public void canCreateContactInGroup() {
        var contact = new ContactData()
                .withFirstname(CommonFunctions.randomString(10))
                .withLastname(CommonFunctions.randomString(10))
                .withPhoto(randomFile("src/test/resources/images"));
        if(app.hbm().getGroupCount() == 0) {
            app.hbm().createGroup(new GroupData("", "group name", "group header", "group footer"));
        }
        var group = app.hbm().getGroupList().get(0);
        var oldRelated = app.hbm().getContactsInGroup(group);
        app.contacts().createContact(contact, group);
        var newRelated = app.hbm().getContactsInGroup(group);
        Assertions.assertEquals(oldRelated.size() + 1, newRelated.size());
    }

    @Test
    public void canAddContactToGroup() {
        // предусловия
        if (app.jdbc().getGroupList().isEmpty()) {
            app.groups().createGroup(new GroupData().withName("DefaultGroup"));
        }
        if (app.jdbc().getContactList().isEmpty()) {
            app.contacts().createContact(new ContactData().withFirstname("AutoTest"));
        }

        var group = app.jdbc().getGroupList().get(0);
        var allContacts = app.jdbc().getContactList();
        var oldRelated = app.jdbc().getContactsInGroup(group);

        // находим контакт, которого нет в этой группе
        var contactToAdd = allContacts.stream()
                .filter(c -> oldRelated.stream().noneMatch(r -> r.id().equals(c.id())))
                .findFirst()
                .orElseGet(() -> {
                    var newContact = new ContactData().withFirstname("Temp").withLastname("ToAdd");
                    app.contacts().createContact(newContact);
                    return app.jdbc().getContactList().getLast();
                });

        // действие через UI
        app.contacts().addContactToGroup(contactToAdd, group);

        // проверка через DB
        var newRelated = app.jdbc().getContactsInGroup(group);
        Assertions.assertEquals(oldRelated.size() + 1, newRelated.size());
        Assertions.assertTrue(newRelated.stream().anyMatch(c -> c.id().equals(contactToAdd.id())));
    }

}
