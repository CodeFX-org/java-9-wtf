# Miscellaneous Pitfalls

## Performance Regression with Noto Sans

Working with Noto Sans fonts on Java 9 takes considerably longer than on Java 8 (at least on Linux).

The root seems to be the creation of the `T2KFontScaler`, which happens deep in the bowels of computing a component's preferred size. If this happens in a tight loop (e.g. to display the size of a list of labels, each sporting a different font), the regression is so devastating that the only solution is to exclude these fonts.

You can observe the effect by compiling and then launching [`NotoSans`](src/wtf/java9/font/NotoSans.java) on Java 8 ...

```bash
javac -d out src/wtf/java9/font/NotoSans.java
java -cp out wtf.java9.font.Fonts
```

... and Java 9 ...

```bash
javac --add-exports java.desktop/sun.font=ALL-UNNAMED -d out src/wtf/java9/font/NotoSans.java
java --add-opens java.desktop/sun.font=ALL-UNNAMED -cp out wtf.java9.font.Fonts
```

At least on 1.8.0_121 vs 9-ea+170-jigsaw-nightly the difference in performance is easily observable.

Two JDK-issues ([JDK-8168288](https://bugs.openjdk.java.net/browse/JDK-8168288) and [JDK-8074562](https://bugs.openjdk.java.net/browse/JDK-8074562)) seem related but are supposed to be resolved and available in ea-170 - so how is this still a thing?
