class User < ActiveRecord::Base
  has_many :rutas_usuario
  has_many :ruta, through: :rutas_usuario
end
