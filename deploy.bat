call nxjc *.java
call nxjlink -o Main.nxj -od Main.nxd Main -v > signatures
call nxjupload Main.nxj