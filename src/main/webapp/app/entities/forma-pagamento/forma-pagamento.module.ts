import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CinematicaSharedModule } from 'app/shared/shared.module';
import { FormaPagamentoComponent } from './forma-pagamento.component';
import { FormaPagamentoDetailComponent } from './forma-pagamento-detail.component';
import { FormaPagamentoUpdateComponent } from './forma-pagamento-update.component';
import { FormaPagamentoDeleteDialogComponent } from './forma-pagamento-delete-dialog.component';
import { formaPagamentoRoute } from './forma-pagamento.route';

@NgModule({
  imports: [CinematicaSharedModule, RouterModule.forChild(formaPagamentoRoute)],
  declarations: [
    FormaPagamentoComponent,
    FormaPagamentoDetailComponent,
    FormaPagamentoUpdateComponent,
    FormaPagamentoDeleteDialogComponent
  ],
  entryComponents: [FormaPagamentoDeleteDialogComponent]
})
export class CinematicaFormaPagamentoModule {}
