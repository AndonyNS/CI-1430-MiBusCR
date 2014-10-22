class Evento < ActiveRecord::Base

  def as_json(options = {})
    super.merge(dia_y_hora: dia_y_hora.strftime('%d.%m.%Y %H:%M'))
  end

end
