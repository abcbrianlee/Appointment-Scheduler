package Model;

public class Reports {

    private int countryCount;
    private String countryName;


    /**
     * @param countryName
     * @param countryCount
     */
    public Reports(String countryName, int countryCount) {
        this.countryCount = countryCount;
        this.countryName = countryName;

    }


    /**
     * Returns country name for custom report.
     * @return countryName
     */
    public String getCountryName() {

        return countryName;
    }

    /**
     * Total for each country.
     * @return countryCount
     */
    public int getCountryCount() {

        return countryCount;
    }

}