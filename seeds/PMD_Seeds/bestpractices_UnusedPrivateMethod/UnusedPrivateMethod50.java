
public class UnusedPrivateMethod {
    public void deleteAllAssetsWithExceptionsNoPurge(int galleryId, Integer... exceptionList) throws MediaServiceException
    {
        deleteAllAssetsWithExceptions(galleryId, false, exceptionList);
    }

    public void deleteAllAssetsWithExceptions(int galleryId, Integer... exceptionList) throws MediaServiceException
    {
        deleteAllAssetsWithExceptions(galleryId, true, exceptionList);
    }

    private void deleteAllAssetsWithExceptions(int galleryId, boolean purge, Integer... exceptionList) throws MediaServiceException
    {
    }
}
        