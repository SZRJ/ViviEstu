import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { FavoritosComponent } from './favoritos/favoritos.component';
import { SimuladorComponent } from './simulador/simulador.component';
import { TiempoService } from './services/tiempo.service';
import { HomeComponent } from './home/home.component';

@NgModule({
  declarations: [AppComponent, FavoritosComponent, SimuladorComponent, HomeComponent],
  imports: [BrowserModule, HttpClientModule, FormsModule, RouterModule.forRoot([
    { path: '', component: HomeComponent },
    { path: 'favoritos', component: FavoritosComponent },
    { path: 'simulador', component: SimuladorComponent }
  ])],
  providers: [TiempoService],
  bootstrap: [AppComponent]
})
export class AppModule {}
