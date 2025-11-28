package com.example.gymweb.model.converter;

import com.example.gymweb.model.Rol;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RolConverter implements AttributeConverter<Rol, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Rol attribute) {
        return attribute != null ? attribute.ordinal() : null;
    }

    @Override
    public Rol convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        Rol[] values = Rol.values();
        if (dbData < 0 || dbData >= values.length) {
            // Si llega un valor desconocido, degradamos a CLIENTE para no romper la carga.
            return Rol.CLIENTE;
        }
        return values[dbData];
    }
}
