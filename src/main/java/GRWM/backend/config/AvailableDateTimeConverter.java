package GRWM.backend.config;

import GRWM.backend.entity.teamplanner.AvailableDateTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.NoArgsConstructor;

import java.util.List;

@Converter(autoApply = false)
@NoArgsConstructor

public class AvailableDateTimeConverter implements AttributeConverter<List<AvailableDateTime>, String> {

    @Override
    public String convertToDatabaseColumn(List<AvailableDateTime> attribute) {
        if (attribute == null) {
            return null;
        }
        // ObjectMapper를 사용하여 List<AvailableDateTime> 객체를 JSON 문자열로 변환
        try {
            return new ObjectMapper().writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // 변환 실패 시 예외 처리 (e.g., RuntimeException 발생)
            throw new RuntimeException("JSON 직렬화 실패", e);
        }
    }


    @Override
    public List<AvailableDateTime> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        // ObjectMapper를 사용하여 JSON 문자열을 List<AvailableDateTime> 객체로 변환
        try {
            // TypeReference를 사용하여 제네릭 타입(List<...>) 정보 전달
            return new ObjectMapper().readValue(dbData, new TypeReference<List<AvailableDateTime>>() {});
        } catch (JsonProcessingException e) {
            // 변환 실패 시 예외 처리
            throw new RuntimeException("JSON 역직렬화 실패", e);
        }
    }
}
