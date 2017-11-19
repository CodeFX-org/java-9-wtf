# Graphics bounds are no longer real

Bounds obtained from the graphics device configuration created from a BufferedImage are no longer real.
Starting from Java 9, they always return (0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE), which are very huge bounds!

```java
System.out.println(new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR).createGraphics().getDeviceConfiguration().getBounds());
```

[The test](src/test/java/wtf/java9/graphics_bounds/GraphicsBoundsTest.java) shows how Java 8 has expected behaviour while Java 9 surprises us.

(Last checked: 8u152 and 9.0.1)

The JDK-issue ([JDK-8072682](https://bugs.openjdk.java.net/browse/JDK-8072682) and the [associated commit](http://hg.openjdk.java.net/jdk9/jdk9/jdk/rev/aafc0a279f95) show this is intentional.
Yet we can seriously question the solution applied to the cache problem described in the bug report.
