package spicy.module;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public enum Category
{
    /**
     * The categories of spicy client.
     */
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player");

    /**
     * The name of categories.
     */
    private String name;


    private Category(final String name) {
        this.name = name;
    }

    /**
     * @return the name of category.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
