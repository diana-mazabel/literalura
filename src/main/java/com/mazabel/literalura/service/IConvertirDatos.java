package com.mazabel.literalura.service;

public interface IConvertirDatos {
    <T> T obtenerDatos (String json, Class<T> clase);
}