package fr.eletutour.ludotheque.dao.bean;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class TypeJeuConverter implements AttributeConverter<Set<TypeJeu>, String> {
    @Override
    public String convertToDatabaseColumn(Set<TypeJeu> typeJeux) {
        if (typeJeux == null || typeJeux.isEmpty()) {
            return "";
        }
        return typeJeux.stream()
                .map(TypeJeu::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<TypeJeu> convertToEntityAttribute(String s) {
        if (s == null || s.trim().isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(s.split(","))
                .map(TypeJeu::valueOf)
                .collect(Collectors.toSet());
    }
}
