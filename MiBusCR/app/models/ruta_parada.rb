class RutaParada < ActiveRecord::Base 
  belongs_to :ruta 
  belongs_to :parada 
end
