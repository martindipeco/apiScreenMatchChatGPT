package com.aluracursos.screenmatch.model;

public enum Categoria {

    ACCION("Action", "Acción"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comedia"),
    CRIMEN("Crime", "Crimen"),
    DRAMA("Drama", "Drama");

    private String categoriaOmdb;
    private String categoriaCastellano;

    Categoria (String cateOmdb, String cateCast)
    {
        this.categoriaOmdb = cateOmdb;
        this.categoriaCastellano = cateCast;
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

    public static Categoria fromCastellano (String text)
    {
        for(Categoria cate : Categoria.values())
        {
            if(cate.categoriaCastellano.equalsIgnoreCase(text))
            {
                return cate;
            }
        }
        throw new IllegalArgumentException("No se encontró la categoría " + text);
    }
}
