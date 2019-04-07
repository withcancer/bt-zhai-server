package site.btrainbow.server.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SortObject {
    @NotNull(message = "Sort Type Cannot Be Null")
    @NotBlank(message = "Sort Type Cannot Be Empty")
    private String value;
    @NotNull(message = "Sort Condition Cannot Be Null")
    private Boolean enabled;
}
