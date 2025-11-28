package com.example.gymweb.model.converter;

import com.example.gymweb.model.Estado;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoConverter implements AttributeConverter<Estado, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Estado attribute) {
        return attribute != null ? attribute.ordinal() : null;
    }

    @Override
    public Estado convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        Estado[] values = Estado.values();
        if (dbData < 0 || dbData >= values.length) {
            // Valor inválido en DB: dejamos al usuario en estado pendiente para evitar fallos.
            return Estado.PENDIENTE;
        }
        return values[dbData];
    }
}
