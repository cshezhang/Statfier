
public class OnlyOneReturn {
    public Try<SearchHit[]> search(final String indexName, final SearchRequest searchRequest) {
        final SearchHit[] empty = new SearchHit[0];
        final Optional<SearchDefinition> searchDefinition = settingsService.getSearchDefinition(indexName);
        return searchDefinition.<Try<SearchHit[]>>map(
            i -> {
                final List<Try<ProviderSearchHit[]>> res = i.getSearchMapping().stream()
                    .peek(m -> LOGGER.debug("Treating backend \"{}\"", m.getProviderRef()))
                    .map(m -> invokeAdapter(getProviderSearchService(m.getProviderRef()), m, searchRequest))
                    .collect(Collectors.toList());
                return TryCollections.pull(res).map(l -> sortReturning(l.stream().collect(ArrayCollectors.arrayMerging(
                        SearchServiceImpl::toSearchHit,
                        SearchHit::getInternalId,
                        Function.identity(),
                        SearchServiceImpl::merge)).orElse(Collections.emptyList()), SearchServiceImpl.searchHitComparator()))
                    .map(list -> list.toArray(empty));
            }).orElse(Try.success(empty));
    }
}
        