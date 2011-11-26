package com.frienemy.services;

import com.frienemy.services.FrienemyServiceListener;

interface FrienemyServiceAPI {
  void addListener(FrienemyServiceListener listener);
  void removeListener(FrienemyServiceListener listener);
}