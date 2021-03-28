package com.luizalabs.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(WiremockExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public abstract class ContextTestMockAll extends AbstractContextMockDataBase {}
