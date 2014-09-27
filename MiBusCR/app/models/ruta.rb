class Ruta < ActiveRecord::Base
  has_many :ruta_parada
  has_many :parada, through: :ruta_parada
  has_many :ruta_user
  has_many :bus
  has_many :gps, through: :bus
  has_many :user, through: :ruta_user
  validates :nombre, presence: true
end
