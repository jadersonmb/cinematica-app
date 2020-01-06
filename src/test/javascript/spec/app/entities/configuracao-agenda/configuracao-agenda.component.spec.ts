import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CinematicaTestModule } from '../../../test.module';
import { ConfiguracaoAgendaComponent } from 'app/entities/configuracao-agenda/configuracao-agenda.component';
import { ConfiguracaoAgendaService } from 'app/entities/configuracao-agenda/configuracao-agenda.service';
import { ConfiguracaoAgenda } from 'app/shared/model/configuracao-agenda.model';

describe('Component Tests', () => {
  describe('ConfiguracaoAgenda Management Component', () => {
    let comp: ConfiguracaoAgendaComponent;
    let fixture: ComponentFixture<ConfiguracaoAgendaComponent>;
    let service: ConfiguracaoAgendaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [ConfiguracaoAgendaComponent],
        providers: []
      })
        .overrideTemplate(ConfiguracaoAgendaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConfiguracaoAgendaComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfiguracaoAgendaService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ConfiguracaoAgenda(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.configuracaoAgenda && comp.configuracaoAgenda[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
