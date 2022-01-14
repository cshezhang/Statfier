
ModelAndView mav = getMyModelAndView();

ModelAndViewAssert.assertViewName(mav, "register");
ModelAndViewAssert.assertModelAttributeValue(mav, "myAttribute", Boolean.TRUE);
ModelAndViewAssert.assertModelAttributeValue(mav, "myAttribute", Boolean.FALSE);
ModelAndViewAssert.assertModelAttributeValue(mav, "myAttribute", myObject);
