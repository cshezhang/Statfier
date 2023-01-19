package com.company;

import static java.io.*; // Violation since it should come before"java.util"
import static java.util.*; // OK

import java.time.*; // OK
import javax.net.*; // OK
// Violation should be separated by space

// OK
// OK

