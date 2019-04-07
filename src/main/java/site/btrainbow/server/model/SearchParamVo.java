package site.btrainbow.server.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SearchParamVo {
    @NotNull(message = "Keyword Cannot Be Null")
    @NotBlank(message = "Keyword Cannot Be empty")
    private String keyword;

    @Max(value = 100, message = "Page Num Should Not Exceed 100")
    private Integer pageNum;

    @Max(value = 10, message = "Page Size Should Not Exceed 10")
    private Integer pageSize;

    @NotNull(message = "Sort Object Cannot Be Null")
    private SortObject scoreSortDirection;

    @NotNull(message = "Sort Object Cannot Be Null")
    private SortObject lengthSortDirection;

    @NotNull(message = "Sort Object Cannot Be Null")
    private SortObject popularitySortDirection;

    @NotNull(message = "Sort Object Cannot Be Null")
    private SortObject createTimeSortDirection;
}
