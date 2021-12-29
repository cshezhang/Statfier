
public class Foo {
    public List<IListObject> getBliCodeByFiscalYear(int fiscalYear) {
       List<IListObject> records = bliCodeCache.get(fiscalYear);
        if (records == null) {
            logger.info("No BLI Code records cached for year : " + fiscalYear + ". Caching now.");
            records = this.selectBLICodeByFiscalYear(fiscalYear);
            bliCodeCache.put(fiscalYear, records);
        }
        if (records == null) {
            logger.error("Could not load BLI Code DATA.  No records retrieved.");
        }
        return records;
    }

    /**
     * Get a list of All BLI Code for given fiscal year.
     * @param fy the selected FY
     * @return an List of BLI Codes
     */
    private List<IListObject> selectBLICodeByFiscalYear(int fy) {

        List<IListObject> result = new ArrayList<IListObject>();
        try {
            result = spiStripFieldService.getBliCodeList(fy);
        } catch (ServiceException se) {
                logger.error("Error getting BLI Code DATA: " + se.getMessage(), se);
        }
        return result;
    }
}
        