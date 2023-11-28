package com.company;

import static java.io.*; // OK
import static java.util.*; // OK

import java.time.*; // OK
import javax.net.*; // violation as it is not included in standard java package group.

// violation
// OK
// OK

