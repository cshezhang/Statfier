
class X {
    void method() {
        try (ClientResponse response = execute(() -> target.request(mediaTypes).delete(), DELETE, new ExpectedResponse(status, required))) {
            // false positive
        }
    }
}
        