package member.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EnrollRequest {
    private Long memberId;
    private Long courseId;
}
