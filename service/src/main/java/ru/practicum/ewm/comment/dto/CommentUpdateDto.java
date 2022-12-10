package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.util.Constants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateDto {

    @Min(0)
    private Integer commentId;

    @NotBlank(message = "Текст комментария не может состоять только из пробелов")
    @Size(min = Constants.MIN_COMMENT_LENGTH, max = Constants.MAX_COMMENT_LENGTH,
            message = "Неверное количество символов в комментарии")
    private String text;

}
