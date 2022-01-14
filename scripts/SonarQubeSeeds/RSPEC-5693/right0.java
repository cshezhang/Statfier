
@Bean(name = "multipartResolver")
public CommonsMultipartResolver multipartResolver() {
  multipartResolver.setMaxUploadSize(8388608); // Compliant (8 MB)
  return multipartResolver;
}
