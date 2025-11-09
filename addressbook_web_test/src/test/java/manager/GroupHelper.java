package manager;

import model.GroupData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;


public class GroupHelper extends HelperBase {

    public GroupHelper(ApplicationManager manager) {
        super(manager);
    }

    public void openGroupsMethod() {
        click(By.linkText("groups"));
    }

    public void createGroup(GroupData group) {
        openGroupsMethod();
        click(By.name("new"));
        type(By.name("group_name"), group.name());
        type(By.name("group_header"), group.header());
        type(By.name("group_footer"), group.footer());
        click(By.name("submit"));
//        click(By.linkText("group page"));
        click(By.linkText("groups"));
    }

    public void modifyGroup(GroupData group, GroupData modifiedGroup) {
        openGroupsMethod();
        selectGroup(group);
        click(By.xpath("(//input[@name=\'edit\'])[2]"));
        click(By.name("group_name"));
        type(By.name("group_name"), modifiedGroup.name());
        click(By.name("group_header"));
        type(By.name("group_header"), "Header1");
        click(By.name("group_footer"));
        type(By.name("group_footer"),"Footer1");
        click(By.name("update"));
        click(By.linkText("groups"));
    }

    public void removeGroup(GroupData group) {
        openGroupsMethod();
        selectGroup(group);
        removeSelectedGroups();
        click(By.linkText("group page"));
    }

    private void selectGroup(GroupData group) {
        click(By.cssSelector(String.format("input[value='%s']", group.id())));
    }

    private void removeSelectedGroups() {
        click(By.name("delete"));
    }

    public int getCount() {
        openGroupsMethod();
        return manager.driver.findElements(By.name("selected[]")).size();
    }

    public void removeAllGroups() {
        openGroupsMethod();
        selectAllGroups();
        removeSelectedGroups();
    }

    private void selectAllGroups() {
        manager.driver
                .findElements(By.name("selected[]"))
                .forEach(WebElement::click);
    }

    public List<GroupData> getList() {
        var groups = new ArrayList<GroupData>();
        var spans = manager.driver.findElements(By.cssSelector("span.group"));
        return spans.stream()
                .map(span -> {
                    var name = span.getText();
                    var checkbox = span.findElement(By.name("selected[]"));
                    var id = checkbox.getAttribute("value");
                    return new GroupData().withId(id).withName(name);
                })
                .toList();
    }
}
