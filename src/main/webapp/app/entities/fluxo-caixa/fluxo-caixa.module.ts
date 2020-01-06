import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CinematicaSharedModule } from 'app/shared/shared.module';
import { FluxoCaixaComponent } from './fluxo-caixa.component';
import { FluxoCaixaDetailComponent } from './fluxo-caixa-detail.component';
import { FluxoCaixaUpdateComponent } from './fluxo-caixa-update.component';
import { FluxoCaixaDeleteDialogComponent } from './fluxo-caixa-delete-dialog.component';
import { fluxoCaixaRoute } from './fluxo-caixa.route';

@NgModule({
  imports: [CinematicaSharedModule, RouterModule.forChild(fluxoCaixaRoute)],
  declarations: [FluxoCaixaComponent, FluxoCaixaDetailComponent, FluxoCaixaUpdateComponent, FluxoCaixaDeleteDialogComponent],
  entryComponents: [FluxoCaixaDeleteDialogComponent]
})
export class CinematicaFluxoCaixaModule {}
