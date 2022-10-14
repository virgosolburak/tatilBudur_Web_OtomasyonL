#Uye Ol Senaryoları

## 1 Üye olmak için Ana Sayfadaki "Giriş Yapın" butonuna tıklama kontrolü
* Giriş yap butonuna tıklanır
* Eposta alanına "test@tatilbudur.com" girilir
* Passwor alanına "123456" girilir
* Sayfayı kapatma butonuna tıklanır

## 2 Üye olmak için Ana Sayfadaki "Giriş Yapın" butonu içinde bulunan "Ücretsiz üye olun" sayfası içinde boş text kontrolü
* Giriş yap butonuna tıklanır
* Eposta alanına "" girilir
* Passwor alanına "" girilir
* Giriş yap menüsünden giriş yap butonuna tıklanır
* Giriş yap menude "Bu alan zorunludur!" hata mesajının geldiği görülür


## 3 Üye olmak için Ana Sayfadaki "Giriş Yapın" butonu içinde bulunan "Ücretsiz üye olun" sayfası içinde "@" olmadan mail adresi giriş kontrolü
* Giriş yap butonuna tıklanır
* Eposta alanına "test.tatilbudur" girilir
* Passwor alanına "" girilir
* Giriş yap menüsünden giriş yap butonuna tıklanır
* Giriş yap menude "Lütfen geçerli bir e-posta giriniz." hata mesajının geldiği görülür

##4 Üye olmak için Ana Sayfadaki "Giriş Yapın" butonu içinde bulunan "Ücretsiz üye olun" sayfası içinde hatalı parola denemesi kontrolü
* Giriş yap butonuna tıklanır
* Ücretsiz üye olun butonu tıklanır
* Eposta alanına "test@tatilbudur" girilir
* Passwor alanına "1334" girilir
* Giriş yap menüsünden giriş yap butonuna tıklanır
* Şifre hata mesajı "Şifreniz en az 8 karakter uzunluğunda olmalı, en az bir büyük harf, bir küçük harf ve sayı içermeli." olduğu görülür

##5 Üye olmak için Ana Sayfadaki "Giriş Yapın" butonu içinde bulunan "Ücretsiz üye olun" sayfası içinde doğru hesap bilgileri ile tekrar üye olma kontrolü
* Giriş yap butonuna tıklanır
* Ücretsiz üye olun butonu tıklanır
* Eposta alanına "test@tatilbudur" girilir
* Passwor alanına "Test1234" girilir
* Giriş yap menüsünden giriş yap butonuna tıklanır
* Şifre hata mesajı "Belirtmiş olduğunuz email adresi ile sistemimizde üyelik bulunmaktadır." olduğu görülür







