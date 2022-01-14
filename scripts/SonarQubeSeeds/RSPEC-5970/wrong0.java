
ModelAndView mav = getMyModelAndView();

Assert.assertEquals("register", mav.getViewName());
Assert.assertTrue((Boolean) mav.getModelMap().get("myAttribute"));
Assert.assertFalse((Boolean) mav.getModelMap().get("myAttribute"));
Assert.assertEquals(myObject, mav.getModelMap().get("myAttribute"));
