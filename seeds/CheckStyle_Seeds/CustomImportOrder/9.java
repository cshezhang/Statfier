package com.company;

import static java.io.*; // OK
import static java.util.*; // OK

import java.time.*; // violation should be in standard package group
// below special import
import javax.net.*; // Violation should be in special import group

// Violation should be in
// THIRD PARTY PACKAGE GROUP
// Violation
// Violation

