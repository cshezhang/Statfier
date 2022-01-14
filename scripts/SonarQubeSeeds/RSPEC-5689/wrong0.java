
public ResponseEntity<String> testResponseEntity() {
  HttpHeaders responseHeaders = new HttpHeaders();
  responseHeaders.set("x-powered-by", "myproduct"); // Sensitive

  return new ResponseEntity<String>("foo", responseHeaders, HttpStatus.CREATED);
}
