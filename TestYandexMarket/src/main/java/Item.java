public class Item {
    private String itemName, itemPrice;

    public Item(String name, String price){
        itemName = name;
        itemPrice = price;
    }

    public String getName() {
        return itemName.toLowerCase();
    }
}
