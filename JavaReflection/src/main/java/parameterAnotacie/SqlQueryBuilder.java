package parameterAnotacie;

import parameterAnotacie.anotacie.Anotacie.*;

import java.util.List;

public class SqlQueryBuilder {

    @Input("ids")
    private List<String> ids;

    @Input("limit")
    private Integer limit;

    @Input("table")
    private String tableName;

    @Input("column")
    private List<String> columnNames;

    public SqlQueryBuilder(List<String> ids, Integer limit, String tableName, List<String> columnNames) {
        this.ids = ids;
        this.limit = limit;
        this.tableName = tableName;
        this.columnNames = columnNames;
    }

    //tu sa input pouziva len na namapovanie (ako dependency injection alebo autowired)
    //selectStatementBuilder nezavisi na ziadnej inej metode, takže ZavisiNa nepotrebujeme
    @Operation("selectBuilder")
    public String selectStatementBuilder(@Input("table") String tableName, @Input("column") List<String> columnNames) {
        String columnsString = columnNames.isEmpty() ? "*" : String.join(",", columnNames);

        return String.format("SELECT %s FROM %s", columnsString, tableName);
    }

    //ak vlozim FinalResult sem tak zase limit sa už nepridá
    @Operation("where")
    public String addWhereClause(@ZavisiNa("selectBuilder") String query, @Input("ids") List<String> ids) {
        if (ids.isEmpty()) {
            return query;
        }

        return String.format("%s WHERE id IN (%s)", query, String.join(",", ids));
    }

    //ak tu prepisem v ZavisiNa where na selectBuilder tak sa where klauzula vôbec nepridá, šikovne
    @FinalResult
    public String addLimit(@ZavisiNa("where")String query, @Input("limit") Integer limit) {
        if (limit == null || limit == 0) {
            return query;
        }

        if (limit < 0) {
            throw new RuntimeException("limit cannot be negative");
        }

        return String.format("%s LIMIT %d", query, limit.intValue());
    }
}
