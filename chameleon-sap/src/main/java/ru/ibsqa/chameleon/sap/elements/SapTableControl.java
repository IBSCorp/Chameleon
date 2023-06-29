package ru.ibsqa.chameleon.sap.elements;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.sap.definitions.repository.MetaSapTableControl;
import ru.ibsqa.chameleon.sap.driver.SapDriver;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import ru.ibsqa.chameleon.sap.driver.SapSupportedDriver;

import static org.junit.jupiter.api.Assertions.fail;

@MetaElement(value = MetaSapTableControl.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapTableControl extends SapElementFacade {

    @Override
    public void type(String type) {
        ActiveXComponent mActiveXComponent = new ActiveXComponent(getVariant().getDispatch());

    }

    public boolean rowExist(String title, String value){
        int columnIndex = getColumnIndex(title);
        int rowIndex = getRowIndexByValue(columnIndex, value);
        if (rowIndex == -1){
            return false;
        }
        return true;
    }

    public void setValue(String searchTitle, String searchValue, String title, String value){
        int columnIndex = getColumnIndex(title);
        int rowIndex = getRowIndexByValue(getColumnIndex(searchTitle), searchValue);
        ActiveXComponent cell = getCell(rowIndex, columnIndex);
        String type = cell.getProperty("type").getString();
        if (type.equals("GuiCTextField")){
            cell.setProperty("text", value);
            return;
        }
        fail(String.format("Не удалось установить значение в поле: [%s]", title));
    }

    private int getRowIndexByValue(int columnIndex, String value){
        for (int i = 0; i < getRowCount(); i++) {
            if (getCell(i, columnIndex).getProperty("text").getString()
                    .equals(value)){
                return i;
            }
        }
        return -1;
    }
    private ActiveXComponent getCell(int row, int column){
        return new ActiveXComponent(new ActiveXComponent(getVariant().getDispatch()).invoke("GetCell", row, column).toDispatch());
    }


    public int getColumnIndex(String title){
        int rowCount = getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String columnTitle = new ActiveXComponent(new ActiveXComponent(new ActiveXComponent(getVariant().getDispatch()).getProperty("Columns").toDispatch()).
                    invoke("item", i).toDispatch()).getProperty("title").toString();
            if (columnTitle.equals(title)){
                return i;
            }
        }
        return -1;
    }
    private String getColumnNameByIndex(int index){
        return  new ActiveXComponent(new ActiveXComponent(getVariant().getDispatch()).getProperty("ColumnOrder").toDispatch()).invoke("ElementAt", index).getString();
    }
    private int getRowCount(){
        return new ActiveXComponent(getVariant().getDispatch()).getProperty("VisibleRowCount").getInt();
    }
    private int getColumnCount(){
        return new ActiveXComponent(new ActiveXComponent(getVariant().getDispatch()).getProperty("Columns").toDispatch()).getProperty("Count").getInt();
    }

    public void doubleClickRow(int row){
        Variant[] variants = new Variant[2];
        variants[0] = new Variant(row);
        variants[1] = new Variant(getColumnNameByIndex(1));
        new ActiveXComponent(getVariant().getDispatch()).invoke("DoubleClick",variants);
    }

    public void test(){
        int columnCount = new ActiveXComponent(getVariant().getDispatch()).getProperty("ColumnCount").getInt();
        for (int i = 0; i < columnCount; i++) {
            String columnName = new ActiveXComponent(new ActiveXComponent(getVariant().getDispatch()).getProperty("ColumnOrder").toDispatch()).invoke("ElementAt", i).getString();
            String columnTitle = new ActiveXComponent(getVariant().getDispatch()).invoke("GetColumnTooltip", columnName).getString();
            System.err.println(columnName + " : " + columnTitle);
        }
    }

    public void selectRowByIndex(int index) {
        ActiveXComponent mActiveXComponent = new ActiveXComponent(getVariant().getDispatch());
        ActiveXComponent row = new ActiveXComponent(mActiveXComponent.invoke("getAbsoluteRow", index).toDispatch());
        row.setProperty("selected", -1);
        doubleClick();
    }

    public void selectRowByParams(int index) {
        ActiveXComponent mActiveXComponent = new ActiveXComponent(getVariant().getDispatch());
        ActiveXComponent row = new ActiveXComponent(mActiveXComponent.invoke("getAbsoluteRow", index).toDispatch());
        row.setProperty("selected", -1);
        doubleClick();
    }

    public void doubleClick(){
        new ActiveXComponent(((SapDriver)getDriver().getWrappedDriver()).getSession().invoke("findById", "wnd[0]").toDispatch()).invoke("sendVKey", 2);
    }
}
