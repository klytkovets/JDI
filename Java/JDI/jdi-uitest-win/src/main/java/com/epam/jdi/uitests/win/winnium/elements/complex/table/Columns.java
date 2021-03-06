package com.epam.jdi.uitests.win.winnium.elements.complex.table;

import com.epam.commons.linqinterfaces.JFuncTTREx;
import com.epam.commons.map.MapArray;
import com.epam.jdi.uitests.core.interfaces.common.IText;
import com.epam.jdi.uitests.core.interfaces.complex.tables.interfaces.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.List;

import static com.epam.commons.LinqUtils.select;
import static com.epam.jdi.uitests.core.settings.JDISettings.exception;
import static com.epam.jdi.uitests.win.winnium.elements.ElementsUtils.timer;

public class Columns extends TableLine implements IColumn {
    public Columns() {
        hasHeader = true;
        hasHeader = true;
        elementIndex = ElementIndexType.Nums;
        headersLocator = By.xpath(".//th");
        headersLocator = By.xpath(".//th");
        defaultTemplate = By.xpath(".//tr/td[%s]");
    }

    protected List<WebElement> getHeadersAction() {
        return table.getWebElement().findElements(headersLocator);
    }

    protected List<WebElement> getCrossFirstLine() {
        return ((Rows)table.rows()).getLineAction(1);
    }

    public final MapArray<String, ICell> getColumn(String colName) {
        try {
            int rowsCount = table.rows().count();
            List<String> headers = select(table.rows().headers(), String::toLowerCase);
            List<WebElement> webColumn = timer().getResultByCondition(
                    () -> getLineAction(colName), els -> els.size() == rowsCount);
            return new MapArray<>(rowsCount,
                    headers::get,
                    value -> table.cell(webColumn.get(value), new Column(colName), new Row(headers.get(value))));
        } catch (Exception | Error ex) {
            throw throwColumnException(colName, ex.getMessage());
        }
    }

    public final List<String> getColumnValue(String colName) {
        try {
            return select(getLineAction(colName), WebElement::getText);
        } catch (Exception | Error ex) {
            throw throwColumnException(colName, ex.getMessage());
        }
    }

    public final MapArray<String, String> getColumnAsText(String colName) {
        return getColumn(colName).toMapArray(IText::getText);
    }

    public MapArray<String, ICell> cellsToColumn(Collection<ICell> cells) {
        return new MapArray<>(cells,
                cell -> headers().get(cell.rowNum() - 1),
                cell -> cell);
    }

    private MapArray<String, MapArray<String, ICell>> withValueByRule(
            Row row, JFuncTTREx<String, String, Boolean> func) {
        Collection<String> rowNames = row.hasName()
                ? table.rows().getRowAsText(row.getName()).where(func).keys()
                : table.rows().getRowAsText(row.getNum()).where(func).keys();
        return new MapArray<>(rowNames, key -> key, this::getColumn);
    }
    public final MapArray<String, MapArray<String, ICell>> withValue(String value, Row row) {
        return withValueByRule(row, (key, val) -> val.equals(value));
    }
    public final MapArray<String, MapArray<String, ICell>> containsValue(String value, Row row) {
        return withValueByRule(row, (key, val) -> val.contains(value));
    }
    public final MapArray<String, MapArray<String, ICell>> matchesRegEx(String regEx, Row row) {
        return withValueByRule(row, (key, val) -> val.matches(regEx));
    }

    public final MapArray<String, ICell> getColumn(int colNum) {
        if (colNum <= 0)
            throw exception("Table indexes starts from 1");
        if (count() < 0 || count() < colNum || colNum <= 0)
            throw exception("Can't Get Column '%s'. [num] > RowsCount(%s).", colNum, count());
        try {
            int rowsCount = table.rows().count();
            List<WebElement> webColumn = timer().getResultByCondition(
                    () -> getLineAction(colNum), els -> els.size() == rowsCount);
            return new MapArray<>(rowsCount,
                    key -> table.rows().headers().get(key),
                    value -> table.cell(webColumn.get(value), new Column(colNum), new Row(value + 1)));
        } catch (Exception | Error ex) {
            throw throwColumnException(colNum + "", ex.getMessage());
        }
    }

    public final List<String> getColumnValue(int colNum) {
        if (colNum <= 0)
            throw exception("Table indexes starts from 1");
        if (count() < 0 || count() < colNum || colNum <= 0)
            throw exception("Can't Get Column '%s'. [num] > RowsCount(%s).", colNum, count());
        try {
            return select(getLineAction(colNum), WebElement::getText);
        } catch (Exception | Error ex) {
            throw throwColumnException(colNum + "", ex.getMessage());
        }
    }

    public final MapArray<String, String> getColumnAsText(int colNum) {
        return getColumn(colNum).toMapArray(IText::getText);
    }

    public MapArray<String, MapArray<String, ICell>> get() {
        return new MapArray<>(headers(), key -> key, this::getColumn);
    }

    private RuntimeException throwColumnException(String columnName, String ex) {
        return exception("Can't Get Column '%s'. Reason: %s", columnName, ex);
    }
}
