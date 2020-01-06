import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CinematicaSharedModule } from 'app/shared/shared.module';
import { ConfiguracaoAgendaComponent } from './configuracao-agenda.component';
import { ConfiguracaoAgendaDetailComponent } from './configuracao-agenda-detail.component';
import { ConfiguracaoAgendaUpdateComponent } from './configuracao-agenda-update.component';
import { ConfiguracaoAgendaDeleteDialogComponent } from './configuracao-agenda-delete-dialog.component';
import { configuracaoAgendaRoute } from './configuracao-agenda.route';

@NgModule({
  imports: [CinematicaSharedModule, RouterModule.forChild(configuracaoAgendaRoute)],
  declarations: [
    ConfiguracaoAgendaComponent,
    ConfiguracaoAgendaDetailComponent,
    ConfiguracaoAgendaUpdateComponent,
    ConfiguracaoAgendaDeleteDialogComponent
  ],
  entryComponents: [ConfiguracaoAgendaDeleteDialogComponent]
})
export class CinematicaConfiguracaoAgendaModule {}
