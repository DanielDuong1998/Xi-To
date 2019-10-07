package com.ss;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import java.security.InvalidParameterException;
import java.util.HashSet;

public class M {
  private static final ArrayMap<Integer, Check> checkMap = new ArrayMap<>();
  public static final ArrayMap<Integer, String> nameMap = new ArrayMap<>();
  private static HashSet<Integer> dupSet = new HashSet<>();
  private static int valueMask = Integer.parseInt("00001111111111111", 2);

  static {
    checkMap.put(2, (a,z) -> {
      for (int i : a) dupSet.add(i&valueMask);
      for (int i : dupSet) {
        int c = 0;
        for (int k : a)
          if (i == (k&valueMask)) c++;
        if (c == 4) return (7<<13) | z; //tu quy
      }
      return (6<<13) | z; //cu
    });

    checkMap.put(3, (a,z) -> {
      for (int i : a) dupSet.add(i&valueMask);
      for (int i : dupSet) {
        int c = 0;
        for (int k : a)
          if (i == (k&valueMask)) c++;
        if (c == 3) return (3<<13) | z; //xam
      }
      return (2<<13) | z; //thu
    });

    checkMap.put(4, (a,z) -> (1<<13) | z); //doi

    checkMap.put(5, (a,z) -> {
      if ((z>>12) == 1 && (z & 15) == 15) //15 = 1111
        return (4<<13) | 15; //sanh
      for (int i = 0; i <= 8; i++)
        if (((z>>i)&31) == 31) // 31 = 11111
          return (4<<13) | z;//sanh
      return z; //thau
    });
  }
//abc xyz
  public static int check(int[] cards) {
    if (cards.length != 5)
      throw new InvalidParameterException("not enough card");

    dupSet.clear();
    int color = cards[0] >> 13;
    int value = cards[0] & valueMask;
    for (int i = 1; i < cards.length; i++) {
      color &= (cards[i] >> 13);
      value |= (cards[i] & valueMask);
    }
    int same = Integer.bitCount(color) == 1 ? 5 : 0;
    int type = checkMap.get(Integer.bitCount(value)) != null ? checkMap.get(Integer.bitCount(value)).check(cards, value) : 0;
    type += same<<13;
    return type;
  }

  public static int compare(int[] patternA, int[] patternB) {
    if (patternA.length != 5 || patternB.length != 5)
      throw new InvalidParameterException("not enough card");
    int rA = check(patternA);
    int rB = check(patternB);
    if ((rA>>13) > (rB>>13)) return 1;
    if ((rA>>13) < (rB>>13)) return -1;
    int tA = rA&valueMask;
    int tB = rB&valueMask;
    if (tA > tB) return 1;
    if (tA < tB) return -1;

    int sA = 0,sB = 0;
    for (int i = 0; i < 5; i++) {
      sA += patternA[i];
      sB += patternB[i];
    }
    if (sA > sB) return 1;
    if (sA < sB) return  -1;

    //something unexpected
    for (int i = 0; i < 5; i++)
      System.out.print(nameMap.get(patternA[i]) + " " + nameMap.get(patternB[i]));
    throw new InvalidParameterException("card duplicate");
  }

  public static int[] move5(int[] pattern) {
    if (pattern.length < 5) throw new InvalidParameterException("pattern can not less than 5 element");
    int step = (1<<pattern.length);
    int[] buffer = new int[5];
    int[] res = null;
    for (int i = 0; i < step; i++) {
      if (Integer.bitCount(i) == 5) {
        int idx = 0;
        for (int j = 0; j < pattern.length; j++) {
          if ((i&(1<<j)) > 0)
            buffer[idx++] = pattern[j];
        }
        if (res == null) {
          res = new int[buffer.length];
          System.arraycopy(buffer, 0, res, 0, buffer.length);
        }
        else {
          if (compare(res, buffer) < 0)
            System.arraycopy(buffer, 0, res, 0, buffer.length);
        }
      }
    }
    return res;
  }

  public static Array<Integer> makeCards() {
    Array<Integer> res = new Array<>();
    nameMap.clear();
    for (int i = 0; i < 13; i++)
      for (int j = 0; j < 4; j++){
        int v = 1 << i;
        int c = (8 >> j) << 13;
        int card = c | v;

        String value = "";
        switch (i) {
          case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: value = (i + 2) + ""; break;
          case 9: value = "J"; break;
          case 10: value = "Q"; break;
          case 11: value = "K"; break;
          case 12: value = "A"; break;
          default: break;
        }

        String color = "";
        switch (c >> 13) {
          case 8: color = "C"; break;
          case 4: color = "R"; break;
          case 2: color = "Ch"; break;
          case 1: color = "B"; break;
          default: break;
        }
        String cardName = value + color;
        nameMap.put(card, cardName);
        res.add(card);
      }
    res.shuffle();
    return res;
  }
  //xxyz

  @FunctionalInterface
  interface Check {
    int check(int[] cards, int zip);
  }
}