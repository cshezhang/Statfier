
@RequestMapping("/delete_user", method = RequestMethod.POST)  // Compliant
public String delete1(String username) {
// state of the application will be changed here
}

@RequestMapping(path = "/delete_user", method = RequestMethod.POST) // Compliant
String delete2(@RequestParam("id") String id) {
// state of the application will be changed here
}
