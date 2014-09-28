class User < ActiveRecord::Base
 has_many :ruta_user
 has_many :ruta, through: :ruta_user
end
