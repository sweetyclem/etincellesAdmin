package com.adminportal.enumeration;

public enum Type {
    Carriere( "Carrière" ), CarriereData( "Carrière Booster Data Marketing" ), Startup( "Startup" );

    private final String name;

    private Type( String s ) {
        name = s;
    }

    public boolean equalsName( String otherName ) {
        // (otherName == null) check is not needed because name.equals(null)
        // returns false
        return name.equals( otherName );
    }

    public String toString() {
        return this.name;
    }
}
