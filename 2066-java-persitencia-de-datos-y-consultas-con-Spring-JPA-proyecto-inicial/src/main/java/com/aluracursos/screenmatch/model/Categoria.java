package com.aluracursos.screenmatch.model;

public enum Categoria {

    ACCION("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    CRIMEN("Crime"),
    DRAMA("Drama");

    private String categoriaOmdb;

    Categoria (String cateOmdb)
    {
        this.categoriaOmdb = cateOmdb;
    }

    public static Categoria fromString (String text)
    {
        for(Categoria cate : Categoria.values())
        {
            if(cate.categoriaOmdb.equalsIgnoreCase(text))
            {
                return cate;
            }
        }
        throw new IllegalArgumentException("No se encontró la categoría " + text);
    }
}
